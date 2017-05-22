package demineur.model;

import demineur.view.View;
import demineur.view.ViewPlateau;
import demineur.view.ViewStatMine;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Valentin.
 */
public class DataUnitTest {

	@Test
	public void testData() {
		Data data = new Data(null);
		Assert.assertEquals("Liste vues vide", 0, data.getViewList().size());
		Assert.assertEquals("Dimension défaut", new PlateauSize(10, 10).toString(), data.getDimension().toString());
		Assert.assertEquals("Taux mine", 10, data.getTauxMine());
	}

	@Test
	public void testGenerateJeu() {
		Data data = new Data(null);
		for (int x = 0; x < data.getDimension().getX(); x++) {
			for (int y = 0; y < data.getDimension().getY(); y++) {
				int nbMines = 0;

				if (x - 1 >= 0) {
					if (y - 1 >= 0 && data.getMineXY(x - 1, y - 1).isMine()) nbMines++;
					if (data.getMineXY(x - 1, y).isMine()) nbMines++;
					if (y + 1 < data.getDimension().getY() && data.getMineXY(x - 1, y + 1).isMine()) nbMines++;
				}
				if (y - 1 >= 0 && data.getMineXY(x, y - 1).isMine()) nbMines++;
				if (y + 1 < data.getDimension().getY() && data.getMineXY(x, y + 1).isMine()) nbMines++;
				if (x + 1 < data.getDimension().getX()) {
					if (y - 1 >= 0 && data.getMineXY(x + 1, y - 1).isMine()) nbMines++;
					if (data.getMineXY(x + 1, y).isMine()) nbMines++;
					if (y + 1 < data.getDimension().getY() && data.getMineXY(x + 1, y + 1).isMine()) nbMines++;
				}

				if (!data.getMineXY(x, y).isMine())
					Assert.assertEquals(nbMines, data.getMineXY(x, y).getNbMineProxim());

				Assert.assertFalse(data.getMineXY(x, y).isFlag());
				Assert.assertTrue(data.getMineXY(x, y).isHide());
			}
		}
	}

	@Test
	public void testGetNbMine() {
		Data data = new Data(null);
		int nbMines = 0;
		for (int x = 0; x < data.getDimension().getX(); x++) {
			for (int y = 0; y < data.getDimension().getY(); y++) {
				if (data.getMineXY(x, y).isMine()) nbMines++;
			}
		}
		Assert.assertEquals(nbMines, data.getNbMine());
		Assert.assertEquals((int) (data.getDimension().getX() * data.getDimension().getY() * (data.getTauxMine() / 100.)), data.getNbMine());
	}

	@Test
	public void testGetNbFlagAndGoodFlag() {
		Data data = new Data(null);
		int nbFlag = 0, nbMineFlag = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < data.getDimension().getY(); y++) {
				data.getMineXY(x, y).setFlag(true);
				if (data.getMineXY(x, y).isMine()) nbMineFlag++;
				nbFlag++;
			}
		}
		int[] result = data.getNbFlagAndGoodFlag();
		Assert.assertEquals(nbFlag, result[0]);
		Assert.assertEquals(nbMineFlag, result[1]);
	}

	@Test
	public void testAddToViewlist() {
		Data data = new Data(null);
		View v1 = new ViewStatMine(data);
		data.addToViewList(v1);

		Assert.assertEquals(1, data.getViewList().size());
		Assert.assertEquals(v1, data.getViewId(0));

		View v2 = new ViewPlateau(data);
		data.addToViewList(v2);

		Assert.assertEquals(2, data.getViewList().size());
		Assert.assertEquals(v2, data.getViewId(1));
	}

	@Test
	public void testSetDimension() {
		Data data = new Data(null);
		data.setDimension(new PlateauSize(24, 28));
		Assert.assertEquals(new PlateauSize(24, 28).toString(), data.getDimension().toString());
	}

	@Test
	public void testJeuPerdu() {
		Data data = new Data(null);
		Assert.assertFalse(data.isJeuPerdu());
		data.setJeuPerdu();
		Assert.assertTrue(data.isJeuPerdu());
	}

	@Test
	public void testVerifJeuGagne() {
		Data data = new Data(null);

		Assert.assertFalse(data.verifJeuGagne());
		for (int x = 0; x < data.getDimension().getX(); x++) {
			for (int y = 0; y < data.getDimension().getY(); y++) {
				if (!data.getMineXY(x, y).isMine()) data.getMineXY(x, y).setHide(false);
			}
		}
		Assert.assertTrue(data.verifJeuGagne());

		data.setJeuPerdu();
		Assert.assertFalse(data.verifJeuGagne());
	}

	@Test
	public void testUnHideAllBomb() {
		Data data = new Data(null);

		for (int x = 0; x < data.getDimension().getX(); x++) {
			for (int y = 0; y < data.getDimension().getY(); y++) {
				Assert.assertTrue(data.getMineXY(x, y).isHide());
			}
		}

		data.unHideAllBomb();

		for (int x = 0; x < data.getDimension().getX(); x++) {
			for (int y = 0; y < data.getDimension().getY(); y++) {
				if (data.getMineXY(x, y).isMine()) {
					Assert.assertFalse(data.getMineXY(x, y).isHide());
				} else {
					Assert.assertTrue(data.getMineXY(x, y).isHide());
				}
			}
		}
	}

	@Test
	public void testInitAndReset() {
		Data data = new Data(null);
		Mine[][] platJeu = new Mine[10][10];

		data.setJeuPerdu();
		for (int x = 0; x < data.getDimension().getX(); x++) {
			for (int y = 0; y < data.getDimension().getY(); y++) {
				platJeu[x][y] = new Mine(data.getMineXY(x, y).isMine());
				data.getMineXY(x, y).setHide(false);
				data.getMineXY(x, y).setFlag(true);
			}
		}

		data.initAndReset();

		boolean identique = true;
		for (int x = 0; x < data.getDimension().getX() && identique; x++) {
			for (int y = 0; y < data.getDimension().getY() && identique; y++) {
				identique = (platJeu[x][y].isMine() && data.getMineXY(x, y).isMine()) || (!platJeu[x][y].isMine() && !data.getMineXY(x, y).isMine());
				Assert.assertFalse(data.getMineXY(x, y).isFlag());
				Assert.assertTrue(data.getMineXY(x, y).isHide());
			}
		}

		Assert.assertFalse(identique);
		Assert.assertFalse(data.isJeuPerdu());
	}

	@Test
	public void testResetSamePart() {
		Data data = new Data(null);
		Mine[][] platJeu = new Mine[10][10];

		data.setJeuPerdu();
		for (int x = 0; x < data.getDimension().getX(); x++) {
			for (int y = 0; y < data.getDimension().getY(); y++) {
				platJeu[x][y] = new Mine(data.getMineXY(x, y).isMine());
				data.getMineXY(x, y).setHide(false);
				data.getMineXY(x, y).setFlag(true);
			}
		}

		data.resetSamePart();

		boolean identique = true;
		for (int x = 0; x < data.getDimension().getX() && identique; x++) {
			for (int y = 0; y < data.getDimension().getY() && identique; y++) {
				identique = (platJeu[x][y].isMine() && data.getMineXY(x, y).isMine()) || (!platJeu[x][y].isMine() && !data.getMineXY(x, y).isMine());
				Assert.assertFalse(data.getMineXY(x, y).isFlag());
				Assert.assertTrue(data.getMineXY(x, y).isHide());
			}
		}

		Assert.assertTrue(identique);
		Assert.assertFalse(data.isJeuPerdu());
	}

	@Test
	public void testSetTauxMine() {
		Data data = new Data(null);
		data.setTauxMine(50);
		data.initAndReset();
		Assert.assertEquals(50, data.getNbMine());

		data.setDimension(new PlateauSize(20, 10));
		data.initAndReset();
		Assert.assertEquals(100, data.getNbMine());
	}
}
