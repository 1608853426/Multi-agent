package app;

import java.awt.*;

/**
 * @author SoonMachine
 */
public class SupplyPacket extends GameObject implements Runnable{
	private boolean live;
	public SupplyPacket(Image img) {
		setImg(img);
		setPosition((Math.random()*6*64+64), -34);
		setHeight(img.getHeight(null));
		setWidth(img.getWidth(null));
	}
	@Override
	public void drawSelf(Graphics g) {
		if(live){
			g.drawImage(getImg(), (int)getX(), (int)getY(), null);
			move();
		}
		checkLocation();
	}

	
	@Override
	public void checkLocation() {
		if(!live||getY()>Constant.GAME_HEIGHT){
			setPosition((Math.random()*6*64+64), -34);
			live=false;
		}
	}

	@Override
	public void move() {
		moveY(getY()+2);
	}
	@Override
	public void run() {
		while(true){
			try{
				Thread.sleep(20000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			live=true;
		}
	}
	public boolean isLive(){
		return live;
	}
	public void setLive(boolean live){
		this.live=live;
	}

}
