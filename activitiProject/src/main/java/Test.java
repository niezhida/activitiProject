import java.util.Date;
import java.util.GregorianCalendar;

public class Test {
	public static void main(String[] args) {
		GregorianCalendar gc=new GregorianCalendar(); 
		gc.setTime(new Date()); 
		gc.add(5,-1);
		System.out.println(gc.getTime());
	}
}
