/*
@author: Jadid Alam
@date: 06-07-2024
@version: 1.0
*/

package org.example.lscinvoicebuilder;

import java.io.*;

public class Storage {
    // data for Storage

    private final String FILENAME = "src/main/java/org/example/lscinvoicebuilder/SavedData/Database.csv";
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
    private String invoiceDate;
    public String getInvoiceDate() {return invoiceDate;}
    public void setInvoiceDate(String invoiceDate1) {this.invoiceDate = invoiceDate1;}

    // registry data
    private String registry = "";
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

        refreshDropdownOptions();
    }

    public void load(int loadId) {
        try (FileReader fr2 = new FileReader(FILENAME);
            BufferedReader br2 = new BufferedReader(fr2)) {
            for (int i = 0; i < this.maxObjects; i++) {
                String line = br2.readLine();

                if (i == loadId-1)
                {
                    String[] data = line.split(",");

                    this.id = Integer.parseInt(data[0]);
                    this.registry = data[1];
                    this.title = data[2];
                    this.fName = data[3];
                    this.lName = data[4];
                    this.address1 = data[5];
                    this.address2 = data[6];
                    this.address3 = data[7];
                    this.postcode = data[8];
                    this.noOfChildren = Integer.parseInt(data[9]);
                    this.studentNames = data[10].split("_");
                    this.studentDOB = data[11].split("_");
                    this.studentHPW = new double[this.noOfChildren];
                    this.studentPPW = new double[this.noOfChildren];
                    this.studentFPW = new double[this.noOfChildren];

                }
            }
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("No data found");
        }

    }

    public void store() {

        this.id = this.maxObjects+1;
        this.maxObjects = this.id;

        try (FileWriter fw = new FileWriter(FILENAME,true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            String studentNamesstr = "";
            String studentDOBstr = "";

            for (int i = 0; i < this.noOfChildren; i++) {
                studentNamesstr += this.studentNames[i] + "_";
                studentDOBstr += this.studentDOB[i] + "_";
            }

            bw.write(this.id+","+this.registry+","+this.title+","+this.fName+","+this.lName+","+this.address1+","+this.address2+","+this.address3+","
                    +this.postcode+","+this.noOfChildren+","+studentNamesstr+","+studentDOBstr+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter fw = new FileWriter(FILENAME_TOTAL_OBJECTS,true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("ID: " + this.id + " " + this.title + " " + this.fName + " " + this.lName + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshDropdownOptions() {
        this.dropdownOptions = new String[this.maxObjects];

        try (FileReader fr = new FileReader(FILENAME_TOTAL_OBJECTS);
             BufferedReader br = new BufferedReader(fr)) {
            int count = 0;
            String line;
            while ((line = br.readLine()) != null) {
                this.dropdownOptions[count] = line;
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int findID(String dropDownOption) {

        for (int i = 1; i <= this.dropdownOptions.length; i++) {
            if (this.dropdownOptions[i-1].contains(dropDownOption)) {
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
