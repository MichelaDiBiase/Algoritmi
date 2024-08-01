package it.unicam.cs.asdl2122.pt2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di Test per la classe NussinovFolder
 *
 * @author Luca Tesei (Template)
 *
 */
class NussinovFolderTest {

    @Test
    final void testGetOneOptimalStructure() {
        String sequence = "GCACGACG";
        NussinovFolder nussinovFolder = new NussinovFolder(sequence);
        assertThrows(IllegalStateException.class, () -> nussinovFolder.getOneOptimalStructure());
        nussinovFolder.fold();
        SecondaryStructure structure = nussinovFolder.getOneOptimalStructure();
        assertFalse(structure == null);
        //Controllo che sia ottimale
        assertEquals(structure.getCardinality(), 3);
    }

    @Test
    final void testIsFolded() {
        String sequence = "GCACGACG";
        NussinovFolder nussinovFolder = new NussinovFolder(sequence);
        nussinovFolder.fold();
        assertTrue(nussinovFolder.isFolded());
    }

}
