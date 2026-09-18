package io.github.fareskingtube.hardcore_revived.gui.screen.custom;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.config.ClientConfig;
import io.github.fareskingtube.hardcore_revived.util.PlayerProfileTextureCache;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PlayerSelectorScreen extends Screen {
    private final List<GameProfile> listedPlayers;
    private final GameProfile self;
    private final Consumer<GameProfile> onSelect;
    private PlayerListWidget listWidget;
    private int panelX, panelY, panelWidth, panelHeight;
    private boolean blurEnabled;
    private boolean darkenEnabled;

    public PlayerSelectorScreen(List<GameProfile> listedPlayers, GameProfile self, Consumer<GameProfile> onSelect) {
        super(Component.translatable("gui." + Constants.MOD_ID + ".player_selector_screen.title"));
        this.listedPlayers = listedPlayers;
        this.self = self;
        this.onSelect = onSelect;
    }

    @Override
    protected void init() {
        super.init();

        EditBox searchField = getSearchFieldWidget();
        searchField.setBordered(false);
        searchField.setHint(Component.translatable("gui." + Constants.MOD_ID + ".player_selector_screen.search").withStyle(ChatFormatting.DARK_GRAY));
        searchField.setResponder(this::refreshList);
        this.addRenderableWidget(searchField);


        this.listWidget = new PlayerListWidget(this.minecraft, this.width, this.height - 85, 42, 20);
        this.addWidget(this.listWidget);
        refreshList("");

        // SCGF (Small Claude Generated Function) IDER what this does
        int padding = 5;
        this.panelX = Math.min(searchField.getX(), this.listWidget.getRowLeft()) - padding;
        this.panelY = searchField.getY() - padding;
        int right = Math.max(searchField.getX() + searchField.getWidth(),
                this.listWidget.getRowLeft() + this.listWidget.getRowWidth()) + padding;
        int bottom = this.listWidget.getY() + this.listWidget.getHeight() + padding;
        this.panelWidth = right - this.panelX;
        this.panelHeight = bottom - this.panelY;

        // Apply blur and darkening based on config
        var config = ClientConfig.HANDLER.instance();
        this.darkenEnabled = config.isApplyDarkening;
        this.blurEnabled = config.isApplyBlur;
    }

    /* The Search field widget with custom styling */
    private @NotNull EditBox getSearchFieldWidget() {
        int viewportWidth = this.width / 2;

        int searchFieldWidth = 175;
        return new EditBox(PlayerSelectorScreen.this.font, (viewportWidth - searchFieldWidth / 2), 20, searchFieldWidth + 10, 20,
                Component.translatable("gui." + Constants.MOD_ID + ".player_selector_screen.search")) {
            @Override
            public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
                RenderSystem.enableBlend();

                context.setColor(0.8f, 0.8f, 0.8f, 0.8f);
                context.blit(
                        ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/menu_list_background.png"),
                        this.getX(), this.getY(),
                        0f, 0f,
                        this.getWidth(), this.getHeight(), 32, 32);
                context.setColor(1.0f, 1.0f, 1.0f, 1f);

                if (this.isHovered()) {
                    context.fill(this.getX(), this.getY(),
                            this.getX() + this.getWidth(), this.getY() + this.getHeight(),
                            0x15FFFFFF);
                }


                context.pose().pushPose();
                context.pose().translate(5, 6.5f, 0);

                super.renderWidget(context, mouseX, mouseY, delta);
                RenderSystem.disableBlend();
                context.setColor(1, 1f, 1f, 1f);
                context.pose().popPose();
            }
        };
    }


    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        listWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (darkenEnabled) this.renderMenuBackground(context);
        if (blurEnabled) this.renderBlurredBackground(delta);

        RenderSystem.enableBlend();
        context.setColor(1.0f, 1.0f, 1.0f, 0.8f);
        context.blit(
                ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/menu_list_background.png"),
                panelX, panelY,
                0f, 0f,
                panelWidth, panelHeight, 32, 32);
        context.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        context.renderOutline(panelX, panelY, panelWidth, panelHeight, 0x6000000);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    /* Takes the search query and shows matching players */
    private void refreshList(String query) {
        listWidget.clear();
        for (GameProfile p : listedPlayers) {
            String name = p.getName();
            if (query.isEmpty() || name.toLowerCase().contains(query.toLowerCase())) {
                boolean isSelf = p.getId().equals(self.getId());
                // TODO: Uncomment after after testing
                // if (isSelf) continue;
                listWidget.addPlayerEntry(p, isSelf, selected -> {
                    onSelect.accept(selected);
                    if (minecraft == null) return;
                    this.minecraft.setScreen(null);
                });
            }
        }
    }

    /* The player list */
    public static class PlayerListWidget extends ContainerObjectSelectionList<PlayerListWidget.Entry> {
        private static final int ENTRY_WIDTH = 175;
        private static final int PADDING = 10;

        public PlayerListWidget(Minecraft client, int width, int height, int top, int itemHeight) {
            super(client, width, height, top, itemHeight);
        }

        /* Disables the black background when opening the menu */
        @Override
        protected void renderListBackground(GuiGraphics context) {
            // super.drawMenuListBackground(context);
        }

        /* Disables the header and footer borders */
        @Override
        protected void renderListSeparators(GuiGraphics context) {

        }

        @Override
        public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
            if (this.children().isEmpty()) {
                int centerX = this.getRowLeft() + this.getRowWidth() / 2;
                int centerY = this.getY() + this.getHeight() / 2 - PADDING * 2 + PADDING / 2;

                context.drawCenteredString(
                        this.minecraft.font,
                        Component.translatable("gui." + Constants.MOD_ID + ".player_selector_screen.no_players"),
                        centerX,
                        centerY,
                        0xAAAAAA
                );
            }
            super.renderWidget(context, mouseX, mouseY, delta);
        }

        /* Centering a div */
        @Override
        public int getRowWidth() {
            return ENTRY_WIDTH + PADDING;
        }

        @Override
        public int getRowLeft() {
            return (this.getX() + this.getWidth() / 2 - this.getRowWidth() / 2) + PADDING / 2;
        }

        /* Add scrollbar */
        @Override
        protected int getScrollbarPosition() {
            return (getRowLeft() + getRowWidth() + 4 - PADDING);
        }

        /* Clears listed entries */
        public void clear() {
            super.clearEntries();
        }

        /* Adds a new entry */
        public void addPlayerEntry(GameProfile player, boolean isSelf, Consumer<GameProfile> onPick) {
            super.addEntry(new Entry(this, player, isSelf, onPick));
        }

        private boolean hasScrollbar() {
            return this.getMaxScroll() > 0;
        }

        /* The entry the button is in the list */
        public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            private final Button selectButton;
            private final PlayerListWidget parent;

            public Entry(PlayerListWidget parent, GameProfile player, boolean isSelf, Consumer<GameProfile> onPick) {
                this.parent = parent;
                this.selectButton = new PlayerButtonWidget(
                        0,
                        0,
                        ENTRY_WIDTH,
                        18,
                        player,
                        isSelf,
                        button -> onPick.accept(player));
            }


            /* Renders the entry */
            @Override
            public void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                int width = parent.hasScrollbar() ? entryWidth - PADDING : entryWidth;
                selectButton.setWidth(width);
                selectButton.setPosition(x, y);
                selectButton.render(context, mouseX, mouseY, tickDelta);
            }

            /* I'm going to be honest IDK what these two do */
            @Override
            public List<? extends NarratableEntry> narratables() {
                return List.of(selectButton);
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return List.of(selectButton);
            }

            /* Renders the button that has a player head and their username */
            public static class PlayerButtonWidget extends Button {
                private final boolean isSelf;
                private final GameProfile player;
                int padding = 8;


                protected PlayerButtonWidget(int x, int y, int width, int height, GameProfile player, boolean isSelf, OnPress onPress) {
                    super(x, y, width, height, Component.literal(""), onPress, DEFAULT_NARRATION);
                    this.player = player;

                    this.isSelf = isSelf;
                }

                /* Called every frame, Also where the text and head texture are actually rendered */
                @Override
                protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
                    drawCustomBackground(context);
                    /* Center a div and draw the head */
                    int headSize = this.getHeight() - padding;
                    int headX = this.getX() + padding / 2;
                    int headY = this.getY() + padding / 2;

                    /* Getting the player's skin texture */
                    GameProfile profileToRender = PlayerProfileTextureCache.resolve(this.player);

                    CompletableFuture<PlayerSkin> skinFuture = Minecraft.getInstance()
                            .getSkinManager()
                            .getOrLoad(profileToRender);

                    PlayerSkin textures = skinFuture.getNow(
                            Minecraft.getInstance().getSkinManager().getInsecureSkin(profileToRender)
                    );

                    PlayerFaceRenderer.draw(context, textures, headX, headY, headSize);

                    /* Draw text */
                    int textX = headX + headSize + 4;
                    int textY = this.getY() + (this.getHeight() - 8) / 2;
                    context.drawString(Minecraft.getInstance().font,
                            isSelf ? this.player.getName() + " (You)" : this.player.getName(), textX, textY, 0xFFFFFF);
                }

                /* Pretty self-explanatory. It draws the background.. Also handles the hover */
                private void drawCustomBackground(GuiGraphics context) {
                    RenderSystem.enableBlend();

                    context.setColor(0.8f, 0.8f, 0.8f, 0.8f);
                    context.blit(
                            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/menu_list_background.png"),
                            this.getX(), this.getY(),
                            0f, 0f,
                            this.getWidth(), 18, 32, 32);
                    context.setColor(1.0f, 1.0f, 1.0f, 1f);

                    if (this.isHovered()) {
                        context.fill(this.getX(), this.getY(),
                                this.getX() + this.getWidth(), this.getY() + this.getHeight(),
                                0x15FFFFFF);
                    }

                    RenderSystem.disableBlend();
                }
            }

        }
    }
}
