package demineur.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Valentin.
 */
public class ItemSelectJeuUnitTest {

	@Test
	public void testItemSelectJeuConst1() {
		ItemSelectJeu item = new ItemSelectJeu("toto", new PlateauSize(12, 14), 28);
		Assert.assertEquals("toto", item.toString());
		Assert.assertEquals(new PlateauSize(12,14).toString(), item.getPlateauSize().toString());
		Assert.assertEquals(28, item.getTauxMine());
		Assert.assertFalse(item.isCustom());
	}

	@Test
	public void testItemSelectJeuConst2() {
		ItemSelectJeu item = new ItemSelectJeu("toto");
		Assert.assertEquals("toto", item.toString());
		Assert.assertTrue(item.isCustom());
	}

	@Test
	public void testIsThisItem() {
		ItemSelectJeu item = new ItemSelectJeu("toto", new PlateauSize(12, 14), 28);

		Assert.assertTrue(item.isThisItem(new PlateauSize(12, 14), 28));

		Assert.assertFalse(item.isThisItem(new PlateauSize(13, 14), 28));
		Assert.assertFalse(item.isThisItem(new PlateauSize(12, 14), 27));
	}
}
