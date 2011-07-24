package org.apache.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.model.GuestbookEntry;
import org.apache.model.DatabaseException;
import org.apache.model.GuestbookDB;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Struts-Action zum Erstellen eines Gaestebuch-Eintrags
 */
public class CreateAction extends ActionSupport {

    
    private static String PATH = "//home/vroni/Documents/badwords.txt";
    private GuestbookEntry entryBean;

    public GuestbookEntry getEntryBean() {
        return entryBean;
    }

    public void setEntryBean(GuestbookEntry entry) {
        entryBean = entry;
    }

    // Validation of input.
    public void validate() {
        if (entryBean.getAuthor().length() == 0) {
            addFieldError("entryBean.author", getText("author.required"));
        } else if (entryBean.getAuthor().length() < 3) {
            addFieldError("entryBean.author", getText("author.size"));
        }
        if (entryBean.getText().length() == 0) {
            addFieldError("entryBean.text", getText("text.required"));
        } else if (entryBean.getText().length() < 5) {
            addFieldError("entryBean.text", getText("text.size"));
        }
    }

    @Override
    public String execute() throws Exception {
        GuestbookDB db = GuestbookDB.getInstance();

        ArrayList<String> blackList = readList(PATH);

        // Take every word of the input.

        String text = getEntryBean().getText();
        String[] tokens = text.split("\\s+");

        StringBuilder str = new StringBuilder();
        // Check if the a word of the blacklist matches a word of the input.
        for (int i = 0; i < tokens.length - 1; ++i) {
            if (blackList.contains(tokens[i].toLowerCase())) {
                for (int j = 0; j < tokens[i].length(); ++j) {
                    str.append("*");
                }
            } else {
                str.append(tokens[i]);
            }
            str.append(" ");
        }

        if (blackList.contains(tokens[tokens.length - 1].toLowerCase())) {
            for (int j = 0; j < tokens[tokens.length - 1].length(); ++j) {
                str.append("*");
            }
        } else {
            str.append(tokens[tokens.length - 1]);
        }

        getEntryBean().setText(str.toString());
        try {

            // ATTENTION: guestbook entries containing new line characters will
            // corrupt the db file.
            // This was probably intended behaviour to easily check the
            // exception handling, but should be fixed
            // for a productive use of this code.

            db.addEntry(getEntryBean());
        } catch (DatabaseException e) {
            addActionError(e.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * This method read the blacklist from a given path.
     * 
     * @param path
     *            The path were the blacklist lies.
     * @return The blacklist in an ArrayList.
     * @throws IOException
     *             If an input or output error occured.
     */
    private ArrayList<String> readList(String path) throws IOException {
        ArrayList<String> blackList = new ArrayList<String>();
        File file = new File(path);
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            boolean end = false;
            while (!end) {

                // Read the file line by line.
                String line = reader.readLine();
                if (line == null) {
                    end = true;
                    break;
                }
                blackList.add(line.toLowerCase());
            }
            reader.close();
        } else {
            System.out.println("Error!");
        }
        return blackList;
    }
}

