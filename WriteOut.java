package HCMM17S1;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriteOut {
	public void writeout(List<Member> members, String fileName){
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName);
			for(int i=0; i<members.size(); i++){
				Member member = members.get(i);
				if(member.getName() != null)
					writer.append("name ").append(member.getName()).append("\n");
				if(member.getBirthday() != null)
					writer.append("birthday ").append(member.getBirthday()).append("\n");
				if(member.getMobile() != null)
					writer.append("mobile ").append(member.getMobile()).append("\n");
				if(member.getPass() != null)
					writer.append("pass ").append(member.getPass()).append("\n");
				if(member.getFee() != null)
					writer.append("fee ").append(member.getFee()).append("\n");
				if(member.getAddress() != null)
					writer.append("address ").append(member.getAddress()).append("\n");
				if(member.getEmail() != null)
					writer.append("email ").append(member.getEmail()).append("\n");
				writer.append("\n");
				
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
