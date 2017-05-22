package demineur.model;

/**
 * Created by Valentin.
 */
public class ItemSelectJeu {

	private String label;
	private PlateauSize dimension;
	private int tauxMine;
	private boolean custom;

	public ItemSelectJeu(String label) {
		this.label = label;
		this.custom = true;
	}

	public ItemSelectJeu(String label, PlateauSize dimension, int tauxMine) {
		this.label = label;
		this.dimension = dimension;
		this.tauxMine = tauxMine;
		this.custom = false;
	}

	public PlateauSize getPlateauSize() {
		return dimension;
	}

	public int getTauxMine() {
		return tauxMine;
	}

	public boolean isCustom() {
		return custom;
	}

	@Override
	public String toString() {
		return label;
	}

	public boolean isThisItem(PlateauSize dimension, int tauxMine) {
		return this.dimension.equals(dimension) && this.tauxMine == tauxMine;
	}
}
