package telran.messaging;

public class MessageBox {
private String text;
public synchronized void setText(String text) {
	while(this.text != null) {
		try {
			this.wait();
		} catch (InterruptedException e) {
			
		}
	}
	this.text = text;
	this.notifyAll();
}
public synchronized String getText() throws InterruptedException {
	while (text == null) {
		this.wait();
	}
	String res = text;
	text = null;
	notifyAll();
	return res;
}
public synchronized String pullText() {
	String res = text;
	text = null;
	notifyAll();
	return res;
}
}