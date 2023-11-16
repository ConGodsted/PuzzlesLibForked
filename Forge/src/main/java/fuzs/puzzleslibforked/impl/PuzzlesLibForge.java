package fuzs.puzzleslibforked.impl;

import fuzs.puzzleslibforked.core.CommonFactories;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(PuzzlesLib.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PuzzlesLibForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CommonFactories.INSTANCE.modConstructor(PuzzlesLib.MOD_ID).accept(new PuzzlesLib());
    }
}
