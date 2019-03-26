package by.radchuk.task4.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@MultipartConfig
@WebServlet(urlPatterns = {"/process"})
public class FileProcessingServlet extends HttpServlet {
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
                            throws ServletException, IOException {
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();
        System.out.println(fileName);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(fileName);
        // ... (do your job here)
    }
}
