package Lecturer;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Arrays;

public abstract class MarkCal implements Mark {
    private DBConnection dbConnector;
    private Connection connection = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    DecimalFormat df = new DecimalFormat("#.##");

    double[] quizScores;
    public String courseID = "";
    public String status = "";
    public String Grade = "";
    public double midtermScore = 0.0;
    public double assessmentScore = 0.0;
    public double finalMarks = 0.0;
    private double CAMarks = 0.0;
    public double quizAvg = 0.0;
    public double calculatedFinalPrac = 0.0;
    public double calculatedFinalTheory = 0.0;
    public double GPA = 0.0;
    public double max = 0.0;
    public double secondmax = 0.0;
    public double thirdmax = 0.0;

    public double calculateQuizAvg(String courseID, double quiz1, double quiz2, double quiz3, double quiz4) {
        if (Arrays.asList("ICT01", "ICT03", "ICT04", "ICT05", "TMS01").contains(courseID)) {
            double[] quizScores = {quiz1, quiz2, quiz3};
            Arrays.sort(quizScores);
            for (int i = 0; i < quizScores.length / 2; i++) {
                double temp = quizScores[i];
                quizScores[i] = quizScores[quizScores.length - 1 - i];
                quizScores[quizScores.length - 1 - i] = temp;
            }
            return Double.parseDouble(df.format(((quizScores[0] + quizScores[1]) / 2.0) * 0.1));
        } else if (courseID.equals("ICT02")) {
            double[] quizScores = {quiz1, quiz2, quiz3, quiz4};
            Arrays.sort(quizScores);
            for (int i = 0; i < quizScores.length / 2; i++) {
                double temp = quizScores[i];
                quizScores[i] = quizScores[quizScores.length - 1 - i];
                quizScores[quizScores.length - 1 - i] = temp;
            }
            return Double.parseDouble(df.format(((quizScores[0] + quizScores[1] + quizScores[2]) / 3.0) * 0.1));
        } else {
            throw new IllegalArgumentException("Invalid course ID");
        }
    }

    public double calculateMidtermScore(String courseID, double midterm) {
        if (Arrays.asList("ICT01", "ICT02").contains(courseID)) {
            return Double.parseDouble(df.format(midterm * 0.2));
        } else {
            return 0.0;
        }
    }

    public double calculateAssessmentScore(String courseID, double assignment01, double assignment02) {
        double score = 0.0;
        if (Arrays.asList("ICT03", "ICT04", "ICT05", "TMS01").contains(courseID)) {
            score = ((assignment01 + assignment02) / 2.0) * 0.2;
        } else if (courseID.equals("ICT02")) {
            score = ((assignment01 + assignment02) / 2.0) * 0.1;
        }
        return Double.parseDouble(df.format(score));
    }

    public double calculateCAMarks(double quizAvg, double midtermScore, double assessmentScore) {
        double caMarks = quizAvg + midtermScore + assessmentScore;
        return Double.parseDouble(df.format(caMarks));
    }

    public String checkEligibility(String courseID, String studentID, double caMarks) {
        String status;
        try {
            dbConnector = new DBConnection();
            connection = dbConnector.getMyConnection();
            String query = "SELECT session, eligibility FROM attendanceEligibility WHERE courseID = ? AND userID = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, courseID);
            pstmt.setString(2, studentID);
            rs = pstmt.executeQuery();

            boolean hasTheory = false, hasPractical = false, eligible = true;
            while (rs.next()) {
                String session = rs.getString("session");
                String eligibility = rs.getString("eligibility");
                if (session.equals("theory")) {
                    hasTheory = true;
                } else if (session.equals("practical")) {
                    hasPractical = true;
                }
                if ("Not Eligible".equals(eligibility)) eligible = false;
            }

            if (hasTheory && hasPractical) {
                status = (eligible && caMarks >= 20) ? "Eligible" : "Not Eligible";
            } else {
                status = (caMarks >= 15) ? "Eligible" : "Not Eligible";
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            status = "Not Eligible";
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException e) {}
        }
        return status;
    }

    public double calculateFinalPracMarks(String eligibility, String courseID, double finalPrac) {
        if (!"Eligible".equals(eligibility)) return 0.0;
        if (Arrays.asList("ICT01", "ICT03").contains(courseID)) {
            return Double.parseDouble(df.format(finalPrac * 0.3));
        } else if (Arrays.asList("ICT04", "ICT05", "TMS01").contains(courseID)) {
            return Double.parseDouble(df.format(finalPrac * 0.4));
        }
        return 0.0;
    }

    public double calculateFinalTheoryMarks(String eligibility, String courseID, double finalTheory) {
        if (!"Eligible".equals(eligibility)) return 0.0;
        if (Arrays.asList("ICT01", "ICT03").contains(courseID)) {
            return Double.parseDouble(df.format(finalTheory * 0.4));
        } else if (Arrays.asList("ICT04", "ICT05", "TMS01").contains(courseID)) {
            return Double.parseDouble(df.format(finalTheory * 0.3));
        } else if (courseID.equals("ICT02")) {
            return Double.parseDouble(df.format(finalTheory * 0.6));
        }
        return 0.0;
    }

    public double calculateFinalMarks(double caMarks, double finalPracMarks, double finalTheoryMarks) {
        double finalMark = caMarks + finalPracMarks + finalTheoryMarks;
        return Double.parseDouble(df.format(finalMark));
    }

    public String getGrade(double finalMark) {
        if (finalMark >= 85) return "A+";
        else if (finalMark >= 75) return "A";
        else if (finalMark >= 70) return "A-";
        else if (finalMark >= 65) return "B+";
        else if (finalMark >= 60) return "B";
        else if (finalMark >= 55) return "B-";
        else if (finalMark >= 50) return "C+";
        else if (finalMark >= 45) return "C";
        else if (finalMark >= 40) return "C-";
        else if (finalMark >= 35) return "D+";
        else return "F";
    }

    public double getGPA(String grade) {
        switch (grade) {
            case "A+": return 4.0;
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            default: return 0.0;
        }
    }

    private class DBConnection {
        public Connection getMyConnection() {
            return null;
        }
    }
}
