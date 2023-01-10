package com.jobify.service.export;

import com.jobify.payload.request.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter
{
    private static final Logger LOGGER = LogManager.getLogger(UserCsvExporter.class.getName());

    /**
     * @param listUserDTOs
     * @param response
     * @throws IOException
     */
    public void export(List<UserDTO> listUserDTOs, HttpServletResponse response) throws IOException
    {
        super.setResponseHeader(response, "text/csv", ".csv", "users_");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                                                     CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "First Name", "Last Name", "Email ID", "Location", "Age"};
        String[] fieldMapping = {"userID", "firstName", "lastName", "emailID", "location", "age"};

        csvWriter.writeHeader(csvHeader);

        for (UserDTO userDTO : listUserDTOs)
        {
            csvWriter.write(userDTO, fieldMapping);
        }
        LOGGER.info("{}", "Csv file downloaded successfully!");
        csvWriter.close();
    }
}
