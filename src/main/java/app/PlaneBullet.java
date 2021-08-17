package app;

import core.tasks.OneTask;

import java.awt.*;

public class PlaneBullet extends Bullet {
	private static Image bullet_img;
	static{
		bullet_img=GameUtil.getImage("images/bullet.png");
	}
	public PlaneBullet(){
		super();
		setHeight(bullet_img.getHeight(null));
		setWidth(bullet_img.getWidth(null));
	}
	@Override
	public void drawSelf(Graphics g) {
		if(isLive()){
			g.drawImage(bullet_img, (int)getX(), (int)getY(), null);
			move();
			checkLocation();
		}
	}
	@Override
	public void checkLocation() {
		if(getY()<0) {
			setLive(false);
		}
	}

	@Override
	public void move() {
		moveY(getY()-Constant.BULLET_STEP);
	}

}
