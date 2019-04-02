package by.radchuk.task4.parser.dom;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.parser.AbstractTagHandler;
import by.radchuk.task4.parser.Storage;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
class TagHandlerAdapter implements Storage {
    private AbstractTagHandler tagHandler;
    public TagHandlerAdapter(final AbstractTagHandler handler) {
        tagHandler = handler;
    }

    @Override
    public Object getData() {
        return tagHandler.getData();
    }

    public void handle(final Document document) throws ParseException {
        log.debug("Handling document ...");
        NodeList list = document.getElementsByTagName("device");
        for (int i = 0; i < list.getLength(); ++i) {
            handleDevice(list.item(i));
        }
        log.debug("Ended document handling.");
    }

    private void handleDevice(final Node node) throws ParseException {
        log.debug("Collecting information from node with name = {}", node.getNodeName());
        boolean isTag = false;
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                NamedNodeMap attributes = node.getAttributes();
                tagHandler.onOpen(node.getNodeName(), IntStream.range(0, attributes.getLength())
                        .boxed()
                        .collect(Collectors
                                .toMap(i -> attributes.item(i).getNodeName(), i -> attributes.item(i).getNodeValue())));
                isTag = true;
                break;
            case Node.TEXT_NODE:
                String text = node.getNodeValue().trim();
                if (text.equals("")) {
                    break;
                }
                tagHandler.onText(text);
                break;
        }
        for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
            handleDevice(node.getChildNodes().item(i));
        }
        if (isTag) {
            tagHandler.onClose(node.getNodeName());
        }
        log.debug("Ended node handling with name = {}", node.getNodeName());
    }
}
