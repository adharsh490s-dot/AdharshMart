import org.mindrot.jbcrypt.BCrypt;
public class HashCheck {
  public static void main(String[] args) {
    System.out.println(BCrypt.hashpw("Password123!", BCrypt.gensalt(12)));
  }
}
