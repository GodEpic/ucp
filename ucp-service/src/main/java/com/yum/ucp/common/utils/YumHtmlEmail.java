package com.yum.ucp.common.utils;

import org.apache.commons.mail.EmailException;

import org.apache.commons.mail.HtmlEmail;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/4/27.
 */
public class YumHtmlEmail extends HtmlEmail
{

    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(YumHtmlEmail.class);

    @Override
    public String embed(File file, String cid) throws EmailException
    {
        String fileName = file.getName();
        String filePath = null;

        try
        {
            filePath = file.getCanonicalPath();
        } catch (IOException var9)
        {
            throw new EmailException("couldn\'t get canonical path for " + fileName, var9);
        }

        if (!file.exists())
        {
            throw new EmailException("file " + filePath + " doesn\'t exist");
        } else if (!file.isFile())
        {
            throw new EmailException("file " + filePath + " isn\'t a normal file");
        } else if (!file.canRead())
        {
            throw new EmailException("file " + filePath + " isn\'t readable");
        }

        if (EmailUtils.isEmpty(fileName))
        {
            throw new EmailException("file name cannot be null or empty");
        }

        if (this.inlineEmbeds.containsKey(fileName))
        {
            try
            {
                InlineImage ii = (InlineImage) this.inlineEmbeds.get(MimeUtility.encodeText(fileName));
                FileDataSource fileDataSource = (FileDataSource) ii.getDataSource();
                String existingFilePath = fileDataSource.getFile().getCanonicalPath();
                if (filePath.equals(existingFilePath))
                {
                    return ii.getCid();
                } else
                {
                    throw new EmailException("embedded name \'" + fileName + "\' is already bound to file " + existingFilePath + "; existing names cannot be rebound");
                }
            } catch (UnsupportedEncodingException e)
            {
                logger.error("error message:{}", e.getMessage());
            } catch (IOException var8)
            {
                throw new EmailException("couldn\'t get canonical path for file " + fileName + "which has already been embedded", var8);
            }

        }
        try
        {
            String name = MimeUtility.encodeText(fileName);
            return this.embed((DataSource) (new FileDataSource(file)), name);
        } catch (UnsupportedEncodingException e)
        {
            logger.error("error message:{}", e.getMessage());
        }
        return "";
    }

    private static class InlineImage
    {

        private String cid;
        private DataSource dataSource;
        private MimeBodyPart mbp;

        public InlineImage(String cid, DataSource dataSource, MimeBodyPart mbp)
        {
            this.cid = cid;
            this.dataSource = dataSource;
            this.mbp = mbp;
        }

        public String getCid()
        {
            return this.cid;
        }

        public DataSource getDataSource()
        {
            return this.dataSource;
        }

        public MimeBodyPart getMbp()
        {
            return this.mbp;
        }

        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            } else if (!(obj instanceof YumHtmlEmail.InlineImage))
            {
                return false;
            } else
            {
                YumHtmlEmail.InlineImage that = (YumHtmlEmail.InlineImage) obj;
                return this.cid.equals(that.cid);
            }
        }

        public int hashCode()
        {
            return this.cid.hashCode();
        }
    }
}
