package demineur.model;

/**
 * Created by Valentin.
 */
public class Mine {

	private boolean flag;
	private boolean hide;
	private boolean mine;
	private int nbMineProxim;

	public Mine() {
		this(false);
	}

	@SuppressWarnings("WeakerAccess")
	public Mine(boolean isMine) {
		this(isMine, 0);
	}

	@SuppressWarnings("WeakerAccess")
	public Mine(boolean isMine, int nbMineProxim) {
		this.mine = isMine;
		this.nbMineProxim = nbMineProxim;
		hide = true;
		flag = false;
	}

	public boolean isMine() {
		return mine;
	}

	public int getNbMineProxim() {
		return nbMineProxim;
	}

	public boolean isHide() {
		return hide;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@SuppressWarnings("SameParameterValue")
	public void setHide(boolean hide) {
		this.hide = hide;
		if (!hide) flag = false;
	}

	@SuppressWarnings("SameParameterValue")
	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public void setNbMineProxim(int nbMineProxim) {
		this.nbMineProxim = nbMineProxim;
	}

}
