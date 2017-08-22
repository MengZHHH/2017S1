package HCMM17S1;
import java.io.*;
import java.util.*;
import java.text.*;


public class Performance {
	private List<Member> members;
// Mainly to read instructionFile and write reportFile, which linked to method 'query' and method'add'.
// Use BufferedReader instead of Scanner is because BufferedReader has more buffer space than Scanner.
// Also it can be used in the way using Scanner.
	public void doPerformace(String instructionFile, String reportFile) {
		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(instructionFile)));
			writer = new FileWriter(reportFile);
			String line = null;
			boolean isFirst = true;
			// Here defines whether every line of the instruqctionFile is start with add, query, sort or delete and
			// replace them with "", then recall the method of'add' and 'query' to make further activities.
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("add"))
					add(line.replace("add ", ""));
				else if (line.startsWith("query")) {
					String str = query(line.replace("query ", ""));
					if(isFirst){
						str += "\n";
						isFirst = false;
					}
					writer.append(str);
					//writer.append("\n");
					writer.flush();
				} else if (line.startsWith("delete"))
					delete(line.replace("delete ", ""));
				else if (line.startsWith("sort"))
					sort(line.replace("sort ", ""));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private void add(String addStr) {
		// The add method split a line of string into an String array, then scan every item in the array
		// and detect the head word of each item, if its value equals to any seven field of the member class,
		// then initial this new member object's fields.
		String[] strs = addStr.split("; " );
		Member member = new Member();
		for(String str : strs){
			if(str.startsWith("name")){
				if(!Validation.ValidName(str.replace("name ", "")))
					return;
				member.setName(str.replace("name ", ""));
			}
			else if(str.startsWith("birthday")){
				String[] birs = str.replace("birthday ", "").replace("-", "/").split("/");
				String bir = new String();
				if(birs[0].length() < 2)
					bir =  bir + "0";
				bir += birs[0]+"/";
				if(birs[1].length() < 2)
					bir = bir + "0";
				bir += birs[1] + "/";
				bir += birs[2];
				if(Validation.ValidDate(bir))
					member.setBirthday(bir);
			}
			else if(str.startsWith("mobile")){
				if(!Validation.ValidMobile(str.replace("mobile ", "")))
					return;
				member.setMobile(str.replace("mobile ", ""));
			}
			else if(str.startsWith("pass")){
				if(Validation.ValidPass(str.replace("pass ", "")))
					member.setPass(str.replace("pass ", ""));
			}
			else if(str.startsWith("fee")){
				String fee = str.replace("fee ", "");
				if(fee.startsWith("$"))
					fee = fee.substring(1);
				member.setFee("$"+new DecimalFormat("#0.00").format(Double.parseDouble(fee)));
			
			}
			else if(str.startsWith("address"))
				member.setAddress(str.replace("address ", ""));
			else if(str.startsWith("email")){
				if(Validation.ValidEmail(str.replace("email ", "")))
					member.setEmail(str.replace("email ", ""));
			}
			else {
				member.setAddress(member.getAddress()+str.trim());
			}
		}
		
		if(member.getAddress() != null){
			if(!Validation.ValidAddress(member.getAddress()))
				member.setAddress(null);
		}
		
		int i=0;
		for(i=0; i<members.size(); i++){
			Member m = members.get(i);
			if(m.getName().equals(member.getName()) && m.getMobile().equals(member.getMobile())){
				// Here are checking is there any similarity between the new member which has just been
				// initialized with each member that is already in the arrayList members. If the mobile and
				// the name of the new member are same as one object of the ArrayList members, then the old one's
				// informations would be replaced by the new one, which was executed as an update.
				if(member.getName() != null)
					m.setName(member.getName());
				if(member.getBirthday() != null){
					m.setBirthday(member.getBirthday());
				}
				if(member.getMobile() != null)
					m.setMobile(member.getMobile());
				if(member.getPass() != null)
					m.setPass(member.getPass());
				if(member.getFee() != null){
					m.setFee(member.getFee());
				}
				if(member.getAddress() != null)
					m.setAddress(member.getAddress());
				if(member.getEmail() != null)
					m.setEmail(member.getEmail());
				
				break;
			}
		}
		// After all the previous action had been executed and no similarity was found between the new and the old,
		// the program will confirm that the new one is completely new therefore add it to the last position of the ArrayList.
		if(i >= members.size() )
			
			members.add(member);

	}
	//Below defines an delete method based on members' name and mobile.
	private void delete(String str) {
		String[] dels = str.split("; ");
		for(int i=0; i<members.size(); i++){
			Member m = members.get(i);
			if(m.getName().equals(dels[0]) && m.getMobile().equals(dels[1]))
				members.remove(i);
		}
	}
	// Same as the add method, firstly it split the string into an array which has been predefined as a string applied to query method.
	// The reason I use stringBuilder is it is more flexible compared to the String(You can keep adding things behind a StringBuilder).
	// The new list is used to do former computing for the output of the query statement.
	private String query(String str){
		String[] strs = str.split(" ");
		StringBuilder sb = new StringBuilder();
		if(strs[0].equals("pass")){
			String q = strs[1];
			List<Member> list = new ArrayList<>();
			for(Member m : members){
				//Checking whether a member's pass is same as what the query asked.
				if(m.getPass() != null && m.getPass().equals(q)){
					list.add(m);
				}
			}
			//Here I used comparator to sort every member in the list in an ascending order based on their mobile.
			list.sort(new Comparator<Member>() {
				@Override
				public int compare(Member o1, Member o2) {
					if(o1.getName().equals(o2.getName())){
						return o1.getMobile().compareTo(o2.getMobile());
					}else{
						return o1.getName().compareTo(o2.getName());
					}
				}
				
			});
			//Whether the query condition is Gold, Silver or Bronze, it can list every member which meet the condition.
			sb.append("----query pass "+q+"----").append("\n");
			double total = 0;
			for(Member member : list){
				if(member.getName() != null)
					sb.append("name ").append(member.getName()).append("\n");
				if(member.getBirthday() != null)
					sb.append("birthday ").append(member.getBirthday()).append("\n");
				if(member.getMobile() != null)
					sb.append("mobile ").append(member.getMobile()).append("\n");
				if(member.getPass() != null)
					sb.append("pass ").append(member.getPass()).append("\n");
				if(member.getFee() != null){
					sb.append("fee ").append(member.getFee()).append("\n");
					total += Double.parseDouble(member.getFee().substring(1));
				}
				if(member.getAddress() != null)
					sb.append("address ").append(member.getAddress()).append("\n");
				if(member.getEmail() != null)
					sb.append("email ").append(member.getEmail()).append("\n");
				sb.append("\n");
			}
			
			sb.append("Total Fee: $").append(new DecimalFormat("#.00").format(total)).append("\n").append("-------------------------\n\n");
			
		}else if(strs[0].equals("age")){
			//If the query condition is "age", it will turns to the execution of calculating ages.
			sb.append("----query age fee----\n");
			sb.append("Total Club Member size: ").append(members.size()).append("\n");
			double one = 0;
			double two = 0;
			double three = 0;
			double four = 0;
			double other = 0;
			for(Member m : members){
				double fee = 0;
				if(m.getFee() != null)
					fee = Double.parseDouble(m.getFee().substring(1));
				if(m.getBirthday() == null){
					other += fee;
				}else{
					String[] bs = m.getBirthday().split("/");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = null;
					try {
						date = sdf.parse(bs[2]+"-"+bs[1]+"-"+bs[0]);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int age = getAgeByBirthday(date);
					if(age<=8)
						one += fee;
					else if(age <= 18)
						two += fee;
					else if(age <= 65)
						three += fee;
					else
						four += fee;
				}
				
			}
			sb.append("Age based fee income distribution\n");
			sb.append("(0,8]: $").append(new DecimalFormat("#0.00").format(one)).append("\n");
			sb.append("(8,18]: $").append(new DecimalFormat("#0.00").format(two)).append("\n");
			sb.append("(18,65]: $").append(new DecimalFormat("#0.00").format(three)).append("\n");
			sb.append("(65,-): $").append(new DecimalFormat("#0.00").format(four)).append("\n");
			sb.append("Unknown: $").append(new DecimalFormat("#0.00").format(other)).append("\n");
			sb.append("---------------------\n");
			sb.append("\n");
		}
		return sb.toString();
	}

	private void sort(String str) {
		if(str.equals("ascending")){

			members.sort(new Comparator<Member>() {
				@Override
				public int compare(Member o1, Member o2) {
					if(o1.getName().equals(o2.getName())){
						return o1.getMobile().compareTo(o2.getMobile());
					}else{
						return o1.getName().compareTo(o2.getName());
					}
				}
				
			});
		}else if(str.equals("descending")){
			members.sort(new Comparator<Member>() {
				@Override
				public int compare(Member o1, Member o2) {
					if(o1.getName().equals(o2.getName())){
						return o2.getMobile().compareTo(o1.getMobile());
					}else{
						return o2.getName().compareTo(o1.getName());
					}
				}
				
			});
		}
	}
	//Below defines how I create the program which can calculate the member's age based on their birthday and the current date.
	private static int getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();
		// Get the date information now using getInstance() function.
		if (cal.before(birthday)) {
			throw new IllegalArgumentException(
					"Birthday date is wrong!");
		}
		// If a members birthday is after the current one, we can say that that member's birthday is wrong, therefore won't be 
		// calculated into the output.
		int y = 0;
		int m = 0;
		int d = 0;
		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH) + 1;
		// The reason I put a"+1 here is when using this method, if the month is January, it will turns out to be"0",
		// for former calculation, an normalization of the month is needed.
		d = cal.get(Calendar.DAY_OF_MONTH);
		if(1==1){
		int CurrentYear = y;
		int CurrentMonth = m;
		int CurrentDoM= d;
		cal.setTime(birthday);
		int yy = 0;
		int mm = 0;
		int dd = 0;
		yy = cal.get(Calendar.YEAR);
		mm = cal.get(Calendar.MONTH) + 1;
		dd = cal.get(Calendar.DAY_OF_MONTH);
		int yearBirth = yy;
		int monthBirth = mm;
		int dayOfMonthBirth = dd;
		int age = CurrentYear - yearBirth;
		// First calculate an draft age by the birthday year.
		if (CurrentMonth <= monthBirth) {
			if (CurrentMonth == monthBirth) {
				// If the month are equal, test the date.
				if (CurrentDoM < dayOfMonthBirth) {
					// If the date now is smaller than the birthday date, it means the member has not met the birthday of
					// current year.
					age--;
				}
			} else {
				// monthNow>monthBirth 
				age--;
			}
		}
		return age;
	}
		return 1;
	}

	
	public List<Member> getList() {
		return members;
	}

	public void setList(List<Member> members) {
		this.members = members;
	}
	
}

