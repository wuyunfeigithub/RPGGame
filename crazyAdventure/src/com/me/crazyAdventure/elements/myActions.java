package com.me.crazyAdventure.elements;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.me.crazyAdventure.elements.Boss.DIRECT;
import com.me.crazyAdventure.screens.GameScreen;
import com.me.crazyAdventure.tools.MyMath;
import com.me.crazyAdventure.tools.Recorder;

public class myActions {

	public static Action moveDown(final Boss boss) {
		return new Action() {
			public boolean act(float delta) {
				boss.y -= 210;
				return true;
			}
		};
	}

	public static Action moveUp(final Boss boss) {
		return new Action() {
			public boolean act(float delta) {
				boss.y += 210;
				return true;
			}
		};
	}

	public static Action moveToHero(final Boss boss) {
		return new Action() {
			public boolean act(float delta) {
				if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) <= 20) {
					boss.isMove = false;
					return false;
				}

				if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) <= Recorder.height / 2) {

					if (Math.abs(Hero.x - boss.x) > 1) {
						if (Hero.x >= boss.x) {
							boss.isMove = true;
							boss.direct = boss.direct.Right;
							return false;
						}
						if (Hero.x < boss.x) {
							boss.isMove = true;
							boss.direct = boss.direct.Left;
							return false;
						}
					} else {
						if (Hero.y >= boss.y) {
							boss.isMove = true;
							boss.direct = boss.direct.Up;
							return false;
						}
						if (Hero.y < boss.y) {
							boss.isMove = true;
							boss.direct = boss.direct.Down;
							return false;
						}
					}
				} else {
					boss.isMove = false;
				}
				return false;
			}
		};
	}

	public static Action moveBack(final Boss boss) {
		return new Action() {
			public boolean act(float delta) {
				if (boss.isMove && !boss.isUpdate) {
					boss.x = boss.birthPosition.x;
					boss.y = boss.birthPosition.y;
					// boss.direct = DIRECT.Right;
					boss.isMove = false;
				}
				return true;
			}
		};
	}

	public static Action moveStyle_1(final Boss boss) {
		return new Action() {
			public boolean act(float delta) {
				if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) <= 200) {
					if (!boss.isMove) {
						boss.isMove = true;
						int num = MathUtils.random(1, 3);
						switch (Hero.direct) {
						case Up:
							switch (num) {
							case 1:
								boss.direct = DIRECT.Up;
								break;
							case 2:
								boss.direct = DIRECT.Left;
								break;
							case 3:
								boss.direct = DIRECT.Right;
								break;
							}
							break;
						case Down:
							switch (num) {
							case 1:
								boss.direct = DIRECT.Down;
								break;
							case 2:
								boss.direct = DIRECT.Left;
								break;
							case 3:
								boss.direct = DIRECT.Right;
								break;
							}
							break;
						case Left:
							switch (num) {
							case 1:
								boss.direct = DIRECT.Down;
								break;
							case 2:
								boss.direct = DIRECT.Left;
								break;
							case 3:
								boss.direct = DIRECT.Up;
								break;
							}
							break;
						case Right:
							switch (num) {
							case 1:
								boss.direct = DIRECT.Down;
								break;
							case 2:
								boss.direct = DIRECT.Up;
								break;
							case 3:
								boss.direct = DIRECT.Right;
								break;
							}
							break;
						}
					}
				} else {
					boss.isMove = false;
				}
				return true;
			}
		};
	}

	
	public static Action attack_1(final Boss boss) {
		return new Action() {
			public boolean act(float delta) {
				if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) <= 200) {
					EnemyManager.enemySet.addActor(AttackFactory
							.creatBossShot(boss));
				}
				return true;
			}
		};
	}

	public static Action attack_2(final Boss boss, final Hero hero,
			final GameScreen screen, final EnemyManager manager) {
		return new Action() {
			public boolean act(float delta) {
				if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) <= 300) {
					Vector2 p1 = new Vector2((hero.x + boss.x) / 2, boss.y);
					Vector2 p2 = new Vector2((hero.x + boss.x) / 2, hero.y);
					Vector2 p3 = new Vector2((hero.x + boss.x) / 2,
							(hero.y + boss.y) / 2);
					// Vector2 p4 = new Vector2(hero.x, hero.y);
					Enemy tmp1 = new Enemy(p1, hero, screen, manager);
					Enemy tmp2 = new Enemy(p2, hero, screen, manager);
					Enemy tmp3 = new Enemy(p3, hero, screen, manager);
					// Enemy tmp4 = new Enemy(p4, hero, screen, manager);
					EnemyManager.enemySet.addActor(tmp1);
					EnemyManager.enemySet.addActor(tmp2);
					EnemyManager.enemySet.addActor(tmp3);
					// EnemyManager.enemySet.addActor(tmp4);
				}
				return true;
			}
		};
	}

	public static Action attack_3(final Boss boss) {
		return new Action() {
			public boolean act(float delta) {
				if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) <= 270
						&& MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) >= 80) {
					EnemyManager.enemySet.addActor(AttackFactory
							.creatBossShot2(boss));
				} else if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) < 80) {
					EnemyManager.enemySet.addActor(AttackFactory
							.creatBossShot(boss));
				} else if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) > 270) {
					EnemyManager.enemySet.addActor(AttackFactory
							.creatBossShot3(boss));
				}
				return true;
			}
		};
	}

	public static Action attack_4(final Boss boss) {
		return new Action() {
			public boolean act(float delta) {
				if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) < 80 && EnemyManager.enemySet.getChildren().size<=1) {
					EnemyManager.enemySet.addActorBefore(boss, AttackFactory
							.creatJieJie(boss));
				} else if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) <= 250
						&& MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) >= 80) {
					EnemyManager.enemySet.addActorBefore(boss, AttackFactory
							.creatBossShot(boss));
					
				} else if (MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) > 250 && MyMath.getLength(Hero.x, Hero.y, boss.x, boss.y) < 350) {
					EnemyManager.enemySet.addActorBefore(boss, AttackFactory
							.creatBossShot4(boss));
				} 
				return true;
			}
		};
	}

	
	public static Action buttonMoveUp(final Actor owner){
		return new Action() {
			public boolean act(float delta) {
				owner.setY(owner.getY()+5);
				return true;
			}
		};
	}
	public static Action buttonMoveDown(final Actor owner){
		return new Action() {
			public boolean act(float delta) {
				owner.setY(owner.getY()-5);
				return true;
			}
		};
	}
}
