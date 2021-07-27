
package app;

import java.awt.*;


public class Bullet extends GameObject {
	private boolean live=false;

	public Bullet(){
		this.setHeight(10);
		this.setWidth(10);
	}

	@Override
	public void drawSelf(Graphics g) {
		if(live){
			Color c=g.getColor();
			g.setColor(Color.YELLOW);
			g.fillOval((int)getX(),(int)getY(),getWidth(), getWidth());
			g.setColor(c);
			move();	
			checkLocation();
		}
	}
	public void setLive(boolean live){
		this.live=live;
	}
	public boolean isLive(){
		return live;
	}
	
	@Override
	public void checkLocation() {
		if(getY()>Constant.GAME_HEIGHT) {
			setLive(false);
		}
	}

	@Override
	public void move() {
		moveY(getY()+Constant.BULLET_STEP);
	}

	
	
}
