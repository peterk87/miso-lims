/*
 * Copyright (c) 2012. The Genome Analysis Centre, Norwich, UK
 * MISO project contacts: Robert Davey, Mario Caccamo @ TGAC
 * *********************************************************************
 *
 * This file is part of MISO.
 *
 * MISO is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MISO is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MISO.  If not, see <http://www.gnu.org/licenses/>.
 *
 * *********************************************************************
 */

package uk.ac.bbsrc.tgac.miso.core.service.submission;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.bbsrc.tgac.miso.core.exception.SubmissionException;
import uk.ac.bbsrc.tgac.miso.core.util.TransmissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: collesa Date: 26/03/12 Time: 13:10 To change this template use File | Settings | File Templates.
 */
public class FTPTransferMethod implements TransferMethod {
  protected static final Logger log = LoggerFactory.getLogger(TransmissionUtils.class);

  private String username;
  private String password;

  public FTPTransferMethod() {
  }

  public FTPTransferMethod(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public FTPUploadReport uploadSequenceData(Set<File> dataFiles, EndPoint endpoint) throws SubmissionException {
    List<FTPUploadJob> FTPUploadList = new ArrayList<FTPUploadJob>();
    try {
      for (File f : dataFiles) {
        FTPClient ftpClient = TransmissionUtils.ftpConnect(endpoint.getDestination().getHost(), username, password);

        FTPUploadJob FTPUploadJob = new FTPUploadJob(f);
        // Have left this code commented in case I need to use it to help sort out the upload progress indicator problem
        // Antony 8/05/2012
        // DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        // get current date time with Date()
        // Date date = new Date();
        // System.out.println(dateFormat.format(date));

        boolean success = TransmissionUtils.ftpPutListen(ftpClient, "anon/", FTPUploadJob.getFile(), false, false,
            FTPUploadJob.getListener());
        if (success) {
          log.info("FTPTransferMethod: upload of " + f.getName() + " successful.");
          // date = new Date();
          // System.out.println(dateFormat.format(date));
        } else {
          log.info("FTPTransferMethod: upload of " + f.getName() + " failed.");
        }

        FTPUploadList.add(FTPUploadJob);
        // System.out.println("Upload Job created for:" + FTPUploadJob.getFile() + " " + FTPUploadJob.getBytesTransferred() +
        // " bytes, or " + FTPUploadJob.getPercentageTransferred()+"% transferred...");
      }
      // boolean uploadSuccessful=TransmissionUtils.ftpPut(ftpClient, "anon/", fileList, true, false);

      FTPUploadReport report = new FTPUploadReport(FTPUploadList);
      report.setStatus("uploading");
      report.setMessage("uploading " + FTPUploadList.size() + " files.");

      return (report);
    } catch (java.io.FileNotFoundException j) {
      log.debug("The specified datafiles could not be found.");
      throw new SubmissionException("DataFiles could not be found:" + j.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      throw new SubmissionException(e.getMessage());
    }
  }
}
