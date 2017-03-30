package com.me.crazyAdventure.elements;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.me.crazyAdventure.tools.Assets;
import com.me.crazyAdventure.tools.MyMath;

public class AttackFactory {

	public static Image creatShot(TextureRegion region, Enemy owner) {

		final Image image = new Image(region);
		image.setPosition(owner.x + owner.width / 3, owner.y + owner.height / 3);
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);

		float target_x = image.getX();
		float target_y = image.getY();

		switch (owner.direct) {
		case Up:
			target_y += owner.height * 2;
			break;
		case Down:
			target_y -= owner.height * 2;
			break;
		case Left:
			target_x -= owner.height * 2;
			break;
		case Right:
			target_x += owner.height * 2;
			break;
		}

		MoveToAction moveTo = Actions.moveTo(target_x, target_y, 0.5f);
		RepeatAction repeat = Actions.repeat(2, Actions.rotateBy(360, 0.25f));
		Action endAction = Actions.run(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				image.getParent().removeActor(image);
			}
		});
		ParallelAction paction = Actions.parallel(moveTo, repeat);
		SequenceAction sequence = Actions.sequence(paction, endAction);
		image.addAction(sequence);

		return image;
	}

	public static Attack creatBossShot(Boss owner) {

		final Attack attack = new Attack(owner, 50, 60, "fire_cloud");
		attack.isLive = true;
		attack.setPosition(owner.x + owner.width / 3, owner.y);

		MoveToAction moveTo = Actions.moveTo(Hero.x, Hero.y, 0.8f);
		Action endAction = Actions.run(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				attack.getParent().removeActor(attack);
			}
		});
		SequenceAction sequence = Actions.sequence(moveTo, endAction);
		attack.addAction(sequence);

		return attack;
	}

	public static Attack creatBossShot2(Boss owner) {
		switch (owner.direct) {
		case Up:
		case Down:
			final Attack attack = new Attack(owner, 351 / 9, 270, "fire_smoke");
			attack.isLive = true;

			DelayAction delay = Actions.delay(0.9f);
			Action endAction = Actions.run(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					attack.getParent().removeActor(attack);
				}
			});
			SequenceAction sequence = Actions.sequence(delay, endAction);
			attack.addAction(sequence);
			return attack;
		case Left:
		case Right:
			final Attack attack1 = new Attack(owner, 270, 351 / 9,
					"fire_smoke_rotate");
			attack1.isLive = true;

			DelayAction delay1 = Actions.delay(0.9f);
			Action endAction1 = Actions.run(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					attack1.getParent().removeActor(attack1);
				}
			});
			SequenceAction sequence1 = Actions.sequence(delay1, endAction1);
			attack1.addAction(sequence1);
			return attack1;
		}

		return null;
	}

	public static Attack creatBossShot3(Boss owner) {

		final Attack attack = new Attack(owner, 240 / 8, 25, "fire_fire");
		attack.isLive = true;
		attack.setPosition(owner.x + owner.width / 3, owner.y);

		MoveToAction moveTo = Actions.moveTo(Hero.x, Hero.y, 1.5f);

		Action endAction = Actions.run(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				attack.getParent().removeActor(attack);
			}
		});
		SequenceAction sequence = Actions.sequence(moveTo, endAction);
		attack.addAction(sequence);

		return attack;
	}

	public static Attack creatBossShot4(Boss owner) {
		final Attack attack = new Attack(owner, 513 / 8, 240, "bigfire");
		attack.isLive = true;
		attack.setPosition(Hero.x, Hero.y);

		DelayAction delay = Actions.delay(0.8f);
		Action endAction = Actions.run(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				attack.getParent().removeActor(attack);
			}
		});
		SequenceAction sequence = Actions.sequence(delay, endAction);
		attack.addAction(sequence);

		return attack;
	}

	public static Image creatJieJie(final Boss owner) {

		final Image image = new Image(Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("jiejie"));
		image.setPosition(owner.x + owner.width / 2 - image.getWidth() / 2,
				owner.y - image.getHeight() / 2);
		final Rectangle attackArea = new Rectangle(image.getX(), image.getY(),
				image.getWidth(), image.getHeight());
		Action endAction = Actions.run(new Runnable() {
			@Override
			public void run() {
				image.setPosition(owner.x + owner.width / 2 - image.getWidth()
						/ 2, owner.y - image.getHeight() / 2);
				attackArea.setPosition(image.getX(), image.getY());

				if (attackArea.overlaps(Hero.heroArea)) {
					Hero.currentHp -= 0.3;
					if (Hero.currentHp < 0) {
						Hero.currentHp = 0;
					}
				}

				if (MyMath.getLength(Hero.x, Hero.y, owner.x, owner.y) > 80) {
					// TODO Auto-generated method stub
					image.getParent().removeActor(image);
				}
			}
		});

		RepeatAction repeat = Actions.forever(endAction);
		image.addAction(repeat);

		image.setName("jiejie");

		return image;
	}

	public static Attack creatTrap(final Trap owner) {

		final Attack attack = new Attack(owner.getX(), owner.getY(), 50, 60,
				"fire_cloud");
		attack.isLive = true;

		MoveToAction moveTo = Actions.moveTo(Hero.x, Hero.y, 0.8f);
		Action endAction = Actions.run(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				attack.getParent().removeActor(attack);
			}
		});
		SequenceAction sequence = Actions.sequence(moveTo, endAction);
		attack.addAction(sequence);

		return attack;
	}

}
