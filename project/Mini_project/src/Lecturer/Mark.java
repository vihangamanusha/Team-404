package Lecturer;

public interface Mark {
    public double calQuizavg(String  Course_code, double Quiz1, double Quiz2, double Quiz3, double Quiz4);
    public double calMidMarks(String  Course_code, double Mid_Practical,double Mid_Theory);
    public double calAssesmentMarks(String  Course_code, double Assessment_Mark);
    public double calCAMarks(double QuizAvg, double MidMarks, double AssessmentMark);
    public String checkEligibility(String Course_code, String Student_id, double CAMarks);
    public double calFinalPracticalMarks(String Eligibility, String Course_code, String Student_id, double FinalPracMarks);
    public double calFinalTheoryMarks(String Final_Theory, String Course_code, String Student_id, double FinalPracMarks);
    public double calFinalMarks(String Eligibility, double CAMarks, double calFinalPracticalMarks, double calFinalTheoryMarks);
    public double calGrade (String Eligibility, double Marks);
    public double calGPA(String Eligibility, double Grade);
}
