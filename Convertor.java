import currency.*; 
import distance.*; 
import time.*;
import java.util.Scanner; 
public class Convertor
{
public static void main (String[] args)
{
int code,currency_code,distance_code,time_code;
Scanner sc = new Scanner(System.in);
CurrencyConvertor cc =new CurrencyConvertor();
DistanceConvertor dc =new DistanceConvertor();
TimeConvertor tc =new TimeConvertor();
System.out.println("Enter the code 1:Currency\n2:Distance\n3:Time"); 
code=sc.nextInt();
if(code==1)
{
System.out.println("Enter the Currecy code 1:Euro\n2:Dollar\n3:Yen"); 
currency_code=sc.nextInt();
if(currency_code == 1)
{
 public void convertInrToEuro();
 public void convertEuroToInr();
}
else if(currency_code == 2)
{
 public void convertInrToDollar(); 
 public void convertDollarToInr();
}
else if(currency_code == 3)
{
 public void convertInrToYen(); 
 public void convertYenToInr();
}
else
{
System.out.println("Invalid Code");
}
}
else if(code==2)
{
System.out.println("Enter the Distance code 1:Meter\n2:Miles"); 
distance_code=sc.nextInt();
if(distance_code == 1)
{
public void convertMeterToKm();
 public void convertKmToMeter();
}
else if(distance_code == 2)
{
public void convertMilesToKm(); 
 public void convertKmToMiles();
}
else
{
System.out.println("Invalid Code");
}
}
else if(code==3)
{
System.out.println("Enter the Time code 1:Minutes\n2:Seconds"); 
time_code=sc.nextInt();
if(time_code == 1)
{
tc.convertHourToMinute();
tc.convertMinuteToHour();
}
else if(time_code == 2)
{
tc.convertHourToSeconds(); 
tc.convertSecondsToHour();
}
else
{ 
System.out.println("Invalid Code");
}
}
else
{
System.out.println("Invalid Code");
}
sc.close();
}
}