package by.radchuk.task4.servlet;

import by.radchuk.task4.model.ResponseMessage;
import by.radchuk.task4.model.ResponseStatus;
import by.radchuk.task4.validator.XMLValidator;
import by.radchuk.task4.writer.ResponseWriter;
import com.google.gson.Gson;

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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@MultipartConfig
@WebServlet(urlPatterns = {"/process"})
public class FileProcessingServlet extends HttpServlet {
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
                            throws ServletException, IOException {
        ResponseMessage message = new ResponseMessage();
        ResponseWriter writer = new ResponseWriter(response);
        //request handle
        String parserType = request.getParameter("parser_type");
        List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());
        List<String> filenames = fileParts.stream().map(part -> new String(part.getSubmittedFileName().getBytes(), StandardCharsets.UTF_8)).collect(Collectors.toList());
        //InputStream xmlStream = fileParts.get(IntStream.range(0, filenames.size()).filter(i -> filenames.get(i).endsWith(".xml")).findFirst().getAsInt()).getInputStream();
        //InputStream xsdStream = fileParts.get(IntStream.range(0, filenames.size()).filter(i -> filenames.get(i).endsWith(".xsd")).findFirst().getAsInt()).getInputStream();
        //checking correctness of the input files
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

        if(xmlStream == null || xsdStream == null) {
            message.setStatus(ResponseStatus.ERROR);
            message.setMessageKey("upload_error");
            writer.write(ResponseStatus.ERROR, message);
            return;
        }
        if(xmlStream.available() == 0 || xsdStream.available() == 0) {
            message.setStatus(ResponseStatus.ERROR);
            message.setMessageKey("upload_empty_error");
            writer.write(ResponseStatus.ERROR, message);
            return;
        }

        //xml validation
        XMLValidator validator = new XMLValidator();
        if(!validator.validateAgainstXSD(xmlStream, xsdStream)) {
            message.setStatus(ResponseStatus.ERROR);
            message.setData(validator.getMessage());
            writer.write(message.getStatus(), message);
            return;
        }

        message.setMessageKey("validation_ok");
        message.setStatus(ResponseStatus.OK);
        writer.write(message.getStatus(), message);
    }


}
