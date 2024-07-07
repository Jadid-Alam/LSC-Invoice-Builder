/*
@author: Jadid Alam
@date: 06-07-2024
@version: 1.0
*/

package org.example.lscinvoicebuilder;

import java.io.*;

public class Storage implements Serializable {
    // data for Storage
    @Serial
    private static final long serialVersionUID = 1L;

    private final String FILENAME = "src/main/java/org/example/lscinvoicebuilder/SavedData/Database.ser";
    private final String FILENAME_TOTAL_OBJECTS = "src/main/java/org/example/lscinvoicebuilder/SavedData/NumberOfData.txt";
    private int maxObjects;
    private String[] dropdownOptions;
    public String[] getDropdownOptions() {return dropdownOptions;}

    private int id;
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    // data for PdfBuilder
    private String pdfLocation = determineDownloadsPath();
    public String getPdfLocation() {return pdfLocation;}
    public void setPdfLocation(String pdfLocation1) {this.pdfLocation = pdfLocation1;}
    private String pdfName;
    public String getPdfName() {return pdfName;}
    public void setPdfName(String pdfName1) {this.pdfName = pdfName1;}
    private Boolean isHoursThere;
    public Boolean getIsHoursThere() {return isHoursThere;}
    public void setIsHoursThere(Boolean isHoursThere1) {this.isHoursThere = isHoursThere1;}

    // registry data
    private String registry;
    public String getRegistry() {return registry;}
    public void setRegistry(String registry1) {this.registry = registry1;}

    // parent details
    private String title;
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    private String fName;
    public String getFName() {return fName;}
    public void setFName(String fName1) {this.fName = fName1;}
    private String lName;
    public String getLName() {return lName;}
    public void setLName(String lName1) {this.lName = lName1;}
    private int noOfChildren;
    public int getNoOfChildren() {return noOfChildren;}
    public void setNoOfChildren(int noOfChildren) {this.noOfChildren = noOfChildren;}

    // address

    private String address1;
    public String getAddress1() {return address1;}
    public void setAddress1(String address1) {this.address1 = address1;}
    private String address2;
    public String getAddress2() {return address2;}
    public void setAddress2(String address2) {this.address2 = address2;}
    private String address3;
    public String getAddress3() {return address3;}
    public void setAddress3(String address3) {this.address3 = address3;}
    private String postcode;
    public String getPostcode() {return postcode;}
    public void setPostcode(String postcode) {this.postcode = postcode;}

    // student details
    private String[] studentNames;
    public String[] getStudentNames() {return studentNames;}
    public void setStudentNames(String[] studentNames1) {this.studentNames = studentNames1;}
    public String getStudentName(int index) {return studentNames[index];}
    public void setStudentName(String studentNames1,int index) {this.studentNames[index] = studentNames1;}

    private String[] studentDOB;
    public String[] getStudentDOBs() {return studentDOB;}
    public void setStudentDOBs(String[] studentDOB1) {this.studentDOB = studentDOB1;}
    public String getStudentDOB(int index) {return studentDOB[index];}
    public void setStudentDOB(String studentDOB1, int index) {this.studentDOB[index] = studentDOB1;}

    private double[] studentHPW;
    public double getStudentHPW(int index) {return studentHPW[index];}
    public void setStudentHPW(double studentHPW1, int index) {this.studentHPW[index] = studentHPW1;}
    private double[] studentPPW;
    public double getStudentPPW(int index) {return studentPPW[index];}
    public void setStudentPPW(double studentPPW1,int index) {this.studentPPW[index] = studentPPW1;}
    private double[] studentFPW;
    public double getStudentFPW(int index) {return studentFPW[index];}
    public void setStudentFPW(double studentFPW1,int index) {this.studentFPW[index] = studentFPW1;}

    // durations
    private String startDate;
    public String getStartDate() {return startDate;}
    public void setStartDate(String startDate1) {this.startDate = startDate1;}
    private String endDate;
    public String getEndDate() {return endDate;}
    public void setEndDate(String endDate1) {this.endDate = endDate1;}

    public Storage() {
        try (FileReader fr = new FileReader(FILENAME_TOTAL_OBJECTS);
             BufferedReader br = new BufferedReader(fr)) {
            int count = 0;
            while (br.readLine() != null) {
                count++;
            }
            this.maxObjects = count;
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.dropdownOptions = new String[this.maxObjects];

        try (FileReader fr1 = new FileReader(FILENAME_TOTAL_OBJECTS);
             BufferedReader br1 = new BufferedReader(fr1)) {

            int i = 0;
            while (br1.readLine() != null) {
                this.dropdownOptions[i] = br1.readLine();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void load(int loadId) {
        try (FileInputStream fileIn = new FileInputStream(FILENAME);
             ObjectInputStream ois = new ObjectInputStream(fileIn)) {

            for (int j = 0; j < this.maxObjects; j++)
            {
                Storage st = (Storage) ois.readObject();

                if (st.getId() == loadId)
                {
                    this.id = st.getId();
                    this.registry = st.getRegistry();
                    this.title = st.getTitle();
                    this.fName = st.getFName();
                    this.lName = st.getLName();
                    this.noOfChildren = st.getNoOfChildren();
                    this.address1 = st.getAddress1();
                    this.address2 = st.getAddress2();
                    this.address3 = st.getAddress3();
                    this.postcode = st.getPostcode();
                    this.studentNames = new String[st.getNoOfChildren()];
                    this.studentDOB = new String[st.getNoOfChildren()];
                    this.studentFPW = new double[st.getNoOfChildren()];
                    this.studentHPW = new double[st.getNoOfChildren()];
                    this.studentPPW = new double[st.getNoOfChildren()];

                    for (int i = 0; i < st.getNoOfChildren(); i++)
                    {
                        this.studentNames[i] = st.getStudentName(i);
                        this.studentDOB[i] = st.getStudentDOB(i);

                    }

                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void store() {

        this.id = this.maxObjects;
        this.maxObjects++;

        try (FileOutputStream fileOut = new FileOutputStream(FILENAME,true);
             ObjectOutputStream osw = new ObjectOutputStream(fileOut)) {
            osw.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter fw = new FileWriter(FILENAME_TOTAL_OBJECTS,true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("ID: " + this.id + " " + this.title + " " + this.fName + " " + this.lName + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshDropdownOptions();
    }

    public void refreshDropdownOptions() {
        this.dropdownOptions = new String[this.maxObjects];

        try (FileReader fr = new FileReader(FILENAME_TOTAL_OBJECTS);
             BufferedReader br = new BufferedReader(fr)) {
            for (int i = 0; i < this.maxObjects; i++) {
                this.dropdownOptions[i] = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int findID(String dropDownOption) {
        for (int i = 0; i < this.maxObjects; i++) {
            if (this.dropdownOptions[i].contains(dropDownOption)) {
                return i;
            }
        }
        return -1;
    }

    private String determineDownloadsPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        if (os.contains("win")) {
            return userHome + "\\Downloads";
        } else if (os.contains("mac")) {
            return userHome + "/Downloads";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return userHome + "/Downloads";
        } else {
            return userHome;
        }
    }

}
