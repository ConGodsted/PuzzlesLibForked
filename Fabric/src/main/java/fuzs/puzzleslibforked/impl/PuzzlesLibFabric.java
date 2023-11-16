package fuzs.puzzleslibforked.impl;

import fuzs.puzzleslibforked.core.CommonFactories;
import net.fabricmc.api.ModInitializer;

public class PuzzlesLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(PuzzlesLib.MOD_ID).accept(new PuzzlesLib());
    }
}
