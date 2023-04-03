/**
 * Email - Class to represent one email.
 */
 
public class Email {
   private String fromMail;
   private String toMail;
   private String dateMail;
   private String subjectMail;
   private String contentMail;

   /** Constructor */
   public Email(
      String _fromMail, String _toMail, String _dateMail, 
      String _subjectMail, String _contentMail) {
      
      fromMail = _fromMail; toMail = _toMail;
      dateMail = _dateMail; subjectMail = _subjectMail;
      contentMail = _contentMail;
   }
   
   /** Accessors for each field */
   String getFromMail() { return fromMail; };
   String getToMail() { return toMail; };
   String getDateMail() { return dateMail; };
   String getSubjectMail() { return subjectMail; };
   String getContentMail() { return contentMail; };

   
   /**
    * toString - return the student info as a String
    */
   public String toString() {
      return ("FROM: " + fromMail + "\nTO: " + toMail + "\nDATE: " + dateMail + "\nSUBJECT: " + subjectMail + "\n\n" + contentMail);
   }
  
}