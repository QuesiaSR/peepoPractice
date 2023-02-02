package me.quesia.peepopractice.mixin.gui.screen;

import me.quesia.peepopractice.PeepoPractice;
import me.quesia.peepopractice.core.category.PracticeCategories;
import me.quesia.peepopractice.core.category.PracticeCategoryUtils;
import me.quesia.peepopractice.mixin.access.MoreOptionsDialogAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    private ButtonWidget quitButton;

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Redirect(method = "initWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/GameMenuScreen;addButton(Lnet/minecraft/client/gui/widget/AbstractButtonWidget;)Lnet/minecraft/client/gui/widget/AbstractButtonWidget;", ordinal = 7))
    private AbstractButtonWidget customButtons(GameMenuScreen instance, AbstractButtonWidget abstractButtonWidget) {
        if (this.client == null) { return null; }

        if (PeepoPractice.CATEGORY.equals(PracticeCategories.EMPTY)) {
            if (abstractButtonWidget instanceof ButtonWidget) {
                ButtonWidget buttonWidget = (ButtonWidget) abstractButtonWidget;
                return this.addButton(buttonWidget);
            }
        }

        int i = -16;

        this.quitButton = new ButtonWidget(
                this.width / 2 - 102,
                this.height / 4 + 120 + i,
                98,
                20,
                new LiteralText("Save & Quit"),
                b -> {
                    b.active = false;
                    PracticeCategoryUtils.quit(true);
                }
        );

        if (!PeepoPractice.HAS_FAST_RESET) {
            this.addButton(this.quitButton);
        }

        return this.addButton(
                new ButtonWidget(
                        this.width / 2 + 4,
                        this.height / 4 + 120 + i,
                        98,
                        20,
                        new LiteralText("Replay Split"),
                        b -> {
                            b.active = false;
                            this.client.openScreen(new CreateWorldScreen(null));
                        }
                )
        );
    }

    @Override
    protected <T extends AbstractButtonWidget> T addButton(T button) {
        if (button.getMessage().getString().equals("menu.quitWorld")) {
            button.setWidth(this.quitButton.getWidth());
            button.x = this.quitButton.x;
            button.y = this.quitButton.y;
            button.setMessage(this.quitButton.getMessage());
            return super.addButton(button);
        }
        return super.addButton(button);
    }
}