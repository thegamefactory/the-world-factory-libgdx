package com.tgf.twf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.BuildingType;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.core.world.World;
import com.tgf.twf.input.BuildingToolButtonListener;
import com.tgf.twf.input.WorldInputListener;
import com.tgf.twf.rendering.CoordinatesTransformer;
import com.tgf.twf.rendering.WorldActor;

/**
 * Entry point; loads assets, create systems responsible for rendering and input processing, implements game loop and ticks systems.
 */
public class TheWorldFactoryGame extends ApplicationAdapter {
    private final World world;
    private final PlayerIntentionApi playerIntentionApi;

    private final CoordinatesTransformer coordinatesTransformer;

    private WorldInputListener worldInputListener;
    private Stage gameStage;

    public TheWorldFactoryGame(final World world) {
        this.world = world;
        this.playerIntentionApi = new PlayerIntentionApi(world);
        this.coordinatesTransformer = CoordinatesTransformer.builder()
                .tileSize(new Vector2f(90, 54))
                .offset(new Vector2f(45, 200))
                .build();
    }

    @Override
    public void create() {
        worldInputListener = new WorldInputListener(playerIntentionApi, coordinatesTransformer);

        gameStage = new Stage();
        Gdx.input.setInputProcessor(gameStage);
        Gdx.input.setInputProcessor(new GameInputProcessor(gameStage));

        final WorldActor worldActor = new WorldActor(world, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        worldActor.addListener(worldInputListener);
        gameStage.addActor(worldActor);

        final Texture fieldButtonTexture = new Texture("field_button.png");
        final ImageButton fieldButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(fieldButtonTexture)));
        fieldButton.addListener(new BuildingToolButtonListener(worldInputListener, BuildingType.FIELD, playerIntentionApi));
        final Texture farmButtonTexture = new Texture("farm_button.png");
        final ImageButton farmButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(farmButtonTexture)));
        farmButton.addListener(new BuildingToolButtonListener(worldInputListener, BuildingType.FARM, playerIntentionApi));

        final Container<Table> tableContainer = new Container<>();
        final float sw = Gdx.graphics.getWidth();
        final float sh = Gdx.graphics.getHeight();

        final float cw = sw * 0.85f;
        final float ch = sh * 0.85f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition(cw / 2f, ch / 2f);
        tableContainer.fillX();

        final Table table = new Table();
        table.row();
        table.add(farmButton);
        table.row();
        table.add(fieldButton);

        tableContainer.setActor(table);

//        gameStage.addActor(fieldButton);
//        gameStage.addActor(farmButton);
        gameStage.addActor(tableContainer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        gameStage.act(Gdx.graphics.getDeltaTime());
        gameStage.draw();
    }

    @Override
    public void dispose() {
    }
}
