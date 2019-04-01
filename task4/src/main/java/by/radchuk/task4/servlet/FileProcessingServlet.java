package by.radchuk.task4.servlet;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.model.ResponseMessage;
import by.radchuk.task4.model.ResponseStatus;
import by.radchuk.task4.parser.AbstractParser;
import by.radchuk.task4.parser.device.DeviceParserFactory;
import by.radchuk.task4.writer.ResponseWriter;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File processing servlet.
 * Provides file processing for index.jsp requests.
 */
@Slf4j
@MultipartConfig
@WebServlet(urlPatterns = {"/process"})
public class FileProcessingServlet extends HttpServlet {
    /**
     * Provides xml parsing and validation for index.jsp post requests.
     * @param request Http request object.
     * @param response Http response object.
     * @throws ServletException in case servlet errors.
     * @throws IOException in case IO exceptions.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
                            throws ServletException, IOException {
        log.info("processing POST request, session id = {}",
                 request.getSession().getId());
        ResponseWriter writer = new ResponseWriter(response);
        //request handle
        String parserType = request.getParameter("parser_type");
        List<Part> fileParts = request.getParts().stream().filter(
                part -> "file".equals(part.getName()))
                              .collect(Collectors.toList()
        );
        List<String> filenames = fileParts.stream().map(
                part -> new String(part.getSubmittedFileName().getBytes(),
                        StandardCharsets.UTF_8)).collect(Collectors.toList()
        );
        //InputStream xmlStream = fileParts.get(IntStream.range(0, filenames.size()).filter(i -> filenames.get(i).endsWith(".xml")).findFirst().getAsInt()).getInputStream();
        //InputStream xsdStream = fileParts.get(IntStream.range(0, filenames.size()).filter(i -> filenames.get(i).endsWith(".xsd")).findFirst().getAsInt()).getInputStream();

        log.info("checking correctness of the input files ...");
        InputStream xmlStream = null;
        InputStream xsdStream = null;
        if (filenames.get(0).endsWith(".xml")) {
            xmlStream = fileParts.get(0).getInputStream();
        } else if (filenames.get(0).endsWith(".xsd")) {
            xsdStream = fileParts.get(0).getInputStream();
        }
        if (filenames.get(1).endsWith(".xml")) {
            xmlStream = fileParts.get(1).getInputStream();
        } else if (filenames.get(1).endsWith(".xsd")) {
            xsdStream = fileParts.get(1).getInputStream();
        }
        if (xmlStream == null || xsdStream == null || filenames.size() != 2) {
            writer.write(ResponseStatus.ERROR,
                         ResponseMessage.builder()
                                        .messageKey("upload_error").build());
            return;
        }
        if (xmlStream.available() == 0 || xsdStream.available() == 0) {
            writer.write(ResponseStatus.ERROR,
                    ResponseMessage.builder()
                                   .messageKey("upload_empty_error").build());
            return;
        }
        log.info("input files are correct, starting xml parsing ...");


        AbstractParser parser = DeviceParserFactory.getInstance()
                                                   .createParser(parserType);
        try {
            Object result = parser.parse(xmlStream, xsdStream);
            log.info("xml was successfully parsed!");
            writer.write(ResponseStatus.TABLE, result);
        } catch (ParseException exception) {
            log.info("got the exception during xml parsing.");
            writer.write(ResponseStatus.ERROR,
                    ResponseMessage.builder()
                                   .data(exception.getMessage()).build());
        } catch (Throwable throwable) {
            writer.write(ResponseStatus.ERROR,
                         ResponseMessage.builder()
                                        .messageKey("unknown_error").build());
        }
    }

}
