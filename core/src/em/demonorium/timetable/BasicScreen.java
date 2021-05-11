package em.demonorium.timetable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroupConstructor;
import em.demonorium.timetable.Utils.VariableSystem.Action;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;
import em.demonorium.timetable.Utils.AlignedGroup.AlignedGroup;
import em.demonorium.timetable.Utils.AlignedGroup.GroupRow;


public abstract class BasicScreen implements Screen {
    private final Color clearColor;
    protected int currentWidth, currentHeight;

    protected final Core core;

    protected Stage stage;
    protected Viewport viewport;
    protected Camera camera;

    protected AlignedGroup UI;

    protected final VariableSystem vars = new VariableSystem();


    public void setFlag(String flag, boolean v) {
        vars.setFlag(flag, v);
    }

    public void setFlag(String flag) {
        vars.setFlag(flag);
    }





    private void init() {

        viewport = new ScreenViewport();
        camera = viewport.getCamera();
        this.stage = new Stage(viewport, core.batch);
        create();
        if (UI != null) {
            stage.setRoot(UI);
        }
        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.BACK) || (keycode == Input.Keys.ESCAPE))
                    core.back();
                return super.keyUp(event, keycode);
            }
        });
    }
    public BasicScreen(Core _core) {
        this.core = _core;
        this.clearColor = _core.colors.getBasicColor("back");
        init();
    }

    public BasicScreen(Core _core, Color color) {
        this.core = _core;
        this.clearColor = color;
        init();
    }

    public abstract void create();

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(clearColor.r,
                            clearColor.g,
                            clearColor.b,
                            clearColor.a);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT | Gdx.gl.GL_DEPTH_BUFFER_BIT);


        //if (Gdx.input.)

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        currentWidth = width;
        currentHeight = height;

        viewport.update(width, height, true);
        if (UI != null) {
            UI.setBounds(0, 0, currentWidth, currentHeight);
        }

    }

    private AlignedGroupConstructor UI_CONSTRUCT;

    public AlignedGroupConstructor beginUI() {
        UI_CONSTRUCT = new AlignedGroupConstructor();
        return UI_CONSTRUCT;
    }

    public void endUI() {
        UI = UI_CONSTRUCT.getGroup();
        UI_CONSTRUCT = null;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }




    public void back() {
    }

    public Core getCore() {
        return core;
    }

    protected GroupRow newRow(float from, float ... list) {
        if (list.length == 0)
            return new GroupRow(from, 1f);
        else
            return new GroupRow(from, list);
    }

    protected void makeUI(GroupRow ... rows) {
        UI = new AlignedGroup(rows);
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}
