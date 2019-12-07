package com.tgf.twf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.BuildingType;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.core.world.World;
import com.tgf.twf.input.BuildingToolButtonListener;
import com.tgf.twf.input.GameInputProcessor;
import com.tgf.twf.input.Tool;
import com.tgf.twf.input.ToolPreview;
import com.tgf.twf.input.WorldInputListener;
import com.tgf.twf.rendering.BuildingTextures;
import com.tgf.twf.rendering.CoordinatesTransformer;
import com.tgf.twf.rendering.WorldActor;

/**
 * Entry point; loads assets, create systems responsible for rendering and input processing, implements game loop and ticks systems.
 */
public class TheWorldFactoryGame extends ApplicationAdapter {
    private static final Vector2f TILE_SIZE = new Vector2f(90, 54);
    private static final float CAMERA_SPEED_PIXELS_PER_SECONDS = 1000.0f;

    private final World world;
    private WorldActor worldActor;
    private final PlayerIntentionApi playerIntentionApi;

    private GameInputProcessor gameInputProcessor;
    private final CoordinatesTransformer coordinatesTransformer;

    private WorldInputListener worldInputListener;
    private Stage gameStage;
    private Table uiLayout;
    private BuildingTextures buildingTextures;

    private Label debugLabel;

    public TheWorldFactoryGame(final World world) {
        this.world = world;
        this.playerIntentionApi = new PlayerIntentionApi(world);
        this.coordinatesTransformer = CoordinatesTransformer.ofTileSize(TILE_SIZE);
    }

    @Override
    public void resize(final int width, final int height) {
        gameStage.getViewport().update(width, height);
        gameStage.getCamera().position.set(0f, 0f, 0f);
        uiLayout.setBounds(-width * 0.5f, -height * 0.5f, width, height);
        worldActor.setBounds(-width * 0.5f, -height * 0.5f, width, height);
        coordinatesTransformer.setOffset(width * 0.5f, height * 0.5f);
        coordinatesTransformer.centerCamera((world.getSize().x - 1) * 0.5f, (world.getSize().y - 1) * 0.5f);
    }

    @Override
    public void create() {
        gameStage = new Stage(new ScreenViewport());
        gameInputProcessor = new GameInputProcessor(gameStage);
        Gdx.input.setInputProcessor(gameInputProcessor);

        final ToolPreview toolPreview = new ToolPreview(Tool.DEFAULT_TOOL, new Vector2f(), coordinatesTransformer);
        worldActor = new WorldActor(world, coordinatesTransformer, toolPreview);
        worldInputListener = new WorldInputListener(playerIntentionApi, coordinatesTransformer, toolPreview);
        worldActor.addListener(worldInputListener);
        gameStage.addActor(worldActor);

        buildingTextures = new BuildingTextures();
        final Texture fieldButtonTexture = new Texture("field_button.png");
        final ImageButton fieldButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(fieldButtonTexture)));
        fieldButton.addListener(new BuildingToolButtonListener(worldInputListener, BuildingType.FIELD, playerIntentionApi, buildingTextures));
        final Texture farmButtonTexture = new Texture("farm_button.png");
        final ImageButton farmButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(farmButtonTexture)));
        farmButton.addListener(new BuildingToolButtonListener(worldInputListener, BuildingType.FARM, playerIntentionApi, buildingTextures));

        final Label.LabelStyle defaultStyle = new Label.LabelStyle();
        final BitmapFont defaultFont = new BitmapFont();
        defaultStyle.font = defaultFont;
        defaultStyle.fontColor = Color.BLACK;

        debugLabel = new Label("", defaultStyle);

        uiLayout = new Table();
        uiLayout.setDebug(true);
        uiLayout.row();
        uiLayout.add(debugLabel).align(Align.left).expandX();
        uiLayout.add(farmButton).align(Align.right);
        uiLayout.row();
        uiLayout.add().align(Align.right);
        uiLayout.add(fieldButton);
        uiLayout.row();
        uiLayout.add().expandY();
        gameStage.addActor(uiLayout);
    }

    @Override
    public void render() {
        debugLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond() +
                "\nnativeHeap: " + Gdx.app.getNativeHeap() + "; javaHeap: " + Gdx.app.getJavaHeap() +
                "\nmouseWorld: " + worldInputListener.getMouseWorld().friendlyFormat() +
                ", mouseScreen: " + worldInputListener.getMouseScreen().friendlyFormat());

        coordinatesTransformer.pan(
                CAMERA_SPEED_PIXELS_PER_SECONDS * gameInputProcessor.horizontalSpeed() * Gdx.graphics.getDeltaTime(),
                CAMERA_SPEED_PIXELS_PER_SECONDS * gameInputProcessor.verticalSpeed() * Gdx.graphics.getDeltaTime()
        );

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        gameStage.act(Gdx.graphics.getDeltaTime());
        gameStage.draw();
    }

    @Override
    public void dispose() {
    }
}
