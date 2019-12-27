package com.tgf.twf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.core.world.World;
import com.tgf.twf.core.world.building.BuildingType;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.input.AgentSelector;
import com.tgf.twf.input.BuildingToolButtonListener;
import com.tgf.twf.input.GameInputProcessor;
import com.tgf.twf.input.Tool;
import com.tgf.twf.input.ToolPreview;
import com.tgf.twf.input.ToolTip;
import com.tgf.twf.input.WorldInputListener;
import com.tgf.twf.rendering.BuildingTextures;
import com.tgf.twf.rendering.CoordinatesTransformer;
import com.tgf.twf.rendering.WorldActor;
import com.tgf.twf.rendering.WorldDrawable;

import java.util.LinkedList;
import java.util.List;

/**
 * Entry point; loads assets, create systems responsible for rendering and input processing, implements game loop and ticks systems.
 */
public class TheWorldFactoryGame extends ApplicationAdapter {
    private static final Vector2f TILE_SIZE = new Vector2f(90, 54);
    private static final float CAMERA_SPEED_PIXELS_PER_SECONDS = 1000.0f;

    private final List<ResizeCallback> resizeCallbacks = new LinkedList<>();
    private final List<RenderCallback> renderCallbacks = new LinkedList<>();
    private final List<Disposable> disposables = new LinkedList<>();

    @FunctionalInterface
    private interface ResizeCallback {
        void resize(final int width, final int height);
    }

    @FunctionalInterface
    private interface RenderCallback {
        void render();
    }

    public TheWorldFactoryGame() {
    }

    @Override
    public void create() {
        final Stage gameStage = new Stage(new ScreenViewport());
        resizeCallbacks.add((width, height) -> {
            gameStage.getViewport().update(width, height);
            gameStage.getCamera().position.set(0f, 0f, 0f);
        });
        disposables.add(gameStage);

        final World world = new World(Rules.WORLD_SIZE);

        final CoordinatesTransformer coordinatesTransformer = CoordinatesTransformer.ofTileSize(TILE_SIZE);
        resizeCallbacks.add((width, height) -> {
            coordinatesTransformer.setOffset(width * 0.5f, height * 0.5f);
            coordinatesTransformer.centerCamera((world.getSize().x - 1) * 0.5f, (world.getSize().y - 1) * 0.5f);
        });

        final Vector2f mouseScreenPosition = new Vector2f();
        final ToolPreview toolPreview = new ToolPreview(Tool.NULL_TOOL, mouseScreenPosition, coordinatesTransformer);
        final BitmapFont defaultFont = new BitmapFont();
        disposables.add(defaultFont);
        final ToolTip toolTip = new ToolTip(coordinatesTransformer, world.getGeoMap(), defaultFont, mouseScreenPosition);
        final AgentSelector agentSelector = AgentSelector.builder()
                .coordinatesTransformer(coordinatesTransformer)
                .geoMap(world.getGeoMap())
                .font(defaultFont)
                .mouseScreenPosition(mouseScreenPosition)
                .build();
        renderCallbacks.add(agentSelector::update);

        final GameInputProcessor gameInputProcessor = new GameInputProcessor(gameStage, toolPreview, toolTip);
        renderCallbacks.add(() -> {
            gameStage.act(Gdx.graphics.getDeltaTime() * gameInputProcessor.getGameSpeed().getSpeedFactor());
            gameStage.draw();
        });

        renderCallbacks.add(() ->
                coordinatesTransformer.pan(
                        CAMERA_SPEED_PIXELS_PER_SECONDS * gameInputProcessor.getHorizontalSpeed() * Gdx.graphics.getDeltaTime(),
                        CAMERA_SPEED_PIXELS_PER_SECONDS * gameInputProcessor.getVerticalSpeed() * Gdx.graphics.getDeltaTime()
                )
        );
        Gdx.input.setInputProcessor(gameInputProcessor);

        final TextureAtlas textureAtlas;
        try {
            textureAtlas = new TextureAtlas(Gdx.files.internal("packedimages/pack.atlas"));
        } catch (final GdxRuntimeException e) {
            throw new IllegalStateException(
                    "Could not load texture atlas, did you forget to pack the textures? Run './gradlew texturePacker' to pack the textures."
            );
        }
        disposables.add(textureAtlas);

        final WorldDrawable worldDrawable = WorldDrawable.builder()
                .coordinatesTransformer(coordinatesTransformer)
                .textureAtlas(textureAtlas)
                .toolPreview(toolPreview)
                .toolTip(toolTip)
                .world(world)
                .build();
        final WorldActor worldActor = new WorldActor(world, worldDrawable);
        resizeCallbacks.add((width, height) -> worldActor.setBounds(-width * 0.5f, -height * 0.5f, width, height));
        gameStage.addActor(worldActor);

        final PlayerIntentionApi playerIntentionApi = new PlayerIntentionApi(world);

        final WorldInputListener worldInputListener = new WorldInputListener(playerIntentionApi, coordinatesTransformer, toolPreview,
                mouseScreenPosition);
        worldActor.addListener(worldInputListener);

        final BuildingTextures buildingTextures = new BuildingTextures(textureAtlas);
        final Sprite fieldButtonTexture = textureAtlas.createSprite("field_button");
        final ImageButton fieldButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(fieldButtonTexture)));
        fieldButton.addListener(new BuildingToolButtonListener(worldInputListener, BuildingType.FIELD, playerIntentionApi, buildingTextures));
        final Sprite farmButtonTexture = textureAtlas.createSprite("farm_button");
        final ImageButton farmButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(farmButtonTexture)));
        farmButton.addListener(new BuildingToolButtonListener(worldInputListener, BuildingType.FARM, playerIntentionApi, buildingTextures));

        final Label.LabelStyle defaultStyle = new Label.LabelStyle();
        defaultStyle.font = defaultFont;
        defaultStyle.fontColor = Color.BLACK;

        final Label debugLabel = new Label("", defaultStyle);
        final Vector2f mouseWorld = new Vector2f();
        this.renderCallbacks.add(() -> {
            coordinatesTransformer.convertScreenToWorld(mouseScreenPosition, mouseWorld);
            debugLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond() +
                    "\nnativeHeap: " + Gdx.app.getNativeHeap() + "; javaHeap: " + Gdx.app.getJavaHeap() +
                    "\nmouseWorld: " + mouseWorld.friendlyFormat() +
                    ", mouseScreen: " + mouseScreenPosition.friendlyFormat());
        });

        final Table uiLayout = new Table();
        resizeCallbacks.add((width, height) -> uiLayout.setBounds(-width * 0.5f, -height * 0.5f, width, height));
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
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        for (final RenderCallback renderCallback : renderCallbacks) {
            renderCallback.render();
        }
    }

    @Override
    public void resize(final int width, final int height) {
        for (final ResizeCallback resizeCallback : resizeCallbacks) {
            resizeCallback.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        for (final Disposable disposable : disposables) {
            disposable.dispose();
        }
    }
}
