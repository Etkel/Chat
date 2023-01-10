package academy.prog;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageList {
	private static final MessageList msgList = new MessageList();

    private final Gson gson;
	private final List<Message> list = new LinkedList<>();
	private final Map<String,List<Message>> privateMap = new HashMap<>();
	private final Set<String> logins = new HashSet<>();
	
	public static MessageList getInstance() {
		return msgList;
	}
  
  	private MessageList() {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}
	
	public synchronized void add(Message m) {
		list.add(m);
	}
	
	public synchronized String toJSON(int n) {
		if (n >= list.size()) return null;
		return gson.toJson(new JsonMessages(list, n));
	}

	public synchronized void addPrivate(String to,Message m) {
		List<Message> list = new LinkedList<>();
		if (privateMap.containsKey(to)) {
			list = privateMap.get(to);
			list.add(m);
		} else list.add(m);
		privateMap.put(to,list);
	}

	public synchronized String toJSONPrivate(int n, String to) {
		if (privateMap.containsKey(to)) {
			List<Message> list = privateMap.get(to);
			if (n >= list.size()) return null;
			return gson.toJson(new JsonMessages(list, n));
		} else return null;
	}

	public synchronized void addLogin(String login) {
		logins.add(login);
	}

	public Map<String, List<Message>> getPrivateMap() {
		return privateMap;
	}

	public Set<String> getLogins() {
		return logins;
	}
}
