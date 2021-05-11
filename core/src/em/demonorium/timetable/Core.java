package em.demonorium.timetable;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import em.demonorium.timetable.Factory.ColorSettings;
import em.demonorium.timetable.Factory.ContainerFactory;
import em.demonorium.timetable.Factory.FontGenerator;
import em.demonorium.timetable.Factory.LabelFactory;
import em.demonorium.timetable.Factory.Locale;
import em.demonorium.timetable.Factory.ScrollPaneFactory;
import em.demonorium.timetable.Factory.TextButtonFactory;
import em.demonorium.timetable.Menu.ColorSelectScreen;
import em.demonorium.timetable.Menu.DayEditScreen;
import em.demonorium.timetable.Menu.EditCategoryMenu;
import em.demonorium.timetable.Menu.GetTimeScreen;
import em.demonorium.timetable.Menu.GlobalTaskList;
import em.demonorium.timetable.Menu.IfScreen;
import em.demonorium.timetable.Menu.LessonEditScreen;
import em.demonorium.timetable.Menu.MainScreen;
import em.demonorium.timetable.Menu.NewTaskScreen;
import em.demonorium.timetable.Menu.RealSubjectScreen;
import em.demonorium.timetable.Menu.SettingsScreen;
import em.demonorium.timetable.Menu.SubjectEditScreen;
import em.demonorium.timetable.Menu.SubjectListScreen;
import em.demonorium.timetable.Menu.TaskElementEditor;
import em.demonorium.timetable.Menu.TimetableSelectScreen;
import em.demonorium.timetable.Menu.WeeksSelectionScreen;
import em.demonorium.timetable.TimeData.DataAPI;
import em.demonorium.timetable.TimeData.Prototypes;
import em.demonorium.timetable.Utils.Input.Default.AndroidKeyboardInputFactory;
import em.demonorium.timetable.Utils.Input.Default.DesktopKeyboardInputFactory;
import em.demonorium.timetable.Utils.Input.KeyboardInputAdapterFactory;
import em.demonorium.timetable.Utils.Input.UniversalFieldStyle;
import em.demonorium.timetable.Utils.VariableSystem.VariableSystem;

/* renamed from: em.demonorium.timetable.Core */
public class Core extends Game {
    public static final String DATAFILE = "data.b";
    public static Logger LOGGER;
    private static GregorianCalendar currentDate;
    public static final FontSettings fontSettings = new FontSettings();
    public static VariableSystem settings;
    public final SystemType SYSTEM_TYPE;
    private float _time = 0.0f;
    private float _time2 = 0.0f;
    SpriteBatch batch;
    public ColorSettings colors;
    public ContainerFactory containerFactory;
    public int height;
    private ArrayList<BasicScreen> history = new ArrayList<>();
    public final KeyboardInputAdapterFactory inputFactory;
    private HashMap<String, BasicScreen> screens = new HashMap<>();
    public int width;

    /* renamed from: em.demonorium.timetable.Core$FontSettings */
    public static class FontSettings {
        public FontGenerator.FontGroup alert;
        public FontGenerator.FontGroup less;
        public FontGenerator.FontGroup main;
        public FontGenerator.FontGroup secondary;
    }

    /* renamed from: em.demonorium.timetable.Core$SystemType */
    public enum SystemType {
        DESKTOP,
        MOBILE
    }

    public Core(SystemType systemType) {
        this.SYSTEM_TYPE = systemType;
        if (systemType != SystemType.DESKTOP) {
            this.inputFactory = new AndroidKeyboardInputFactory();
        } else {
            this.inputFactory = new DesktopKeyboardInputFactory();
        }
    }

//    /* renamed from: em.demonorium.timetable.Core$1 */
//    static class C01771 {
//        static final  int[] $SwitchMap$em$demonorium$timetable$Core$SystemType = new int[SystemType.values().length];
//
//        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
//        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
//            return;
//         */
//        /* JADX WARNING: Failed to process nested try/catch */
//        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
//        static {
//            /*
//                em.demonorium.timetable.Core$SystemType[] r0 = em.demonorium.timetable.Core.SystemType.values()
//                int r0 = r0.length
//                int[] r0 = new int[r0]
//                $SwitchMap$em$demonorium$timetable$Core$SystemType = r0
//                int[] r0 = $SwitchMap$em$demonorium$timetable$Core$SystemType     // Catch:{ NoSuchFieldError -> 0x0014 }
//                em.demonorium.timetable.Core$SystemType r1 = em.demonorium.timetable.Core.SystemType.DESKTOP     // Catch:{ NoSuchFieldError -> 0x0014 }
//                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
//                r2 = 1
//                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
//            L_0x0014:
//                int[] r0 = $SwitchMap$em$demonorium$timetable$Core$SystemType     // Catch:{ NoSuchFieldError -> 0x001f }
//                em.demonorium.timetable.Core$SystemType r1 = em.demonorium.timetable.Core.SystemType.MOBILE     // Catch:{ NoSuchFieldError -> 0x001f }
//                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
//                r2 = 2
//                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
//            L_0x001f:
//                return
//            */
//            throw new UnsupportedOperationException("Method not decompiled: em.demonorium.timetable.Core.C01771.<clinit>():void");
//        }
//    }

    private void setTextButtonST(TextButton.TextButtonStyle textButtonStyle, BitmapFont bitmapFont, String str) {
        textButtonStyle.checked = this.colors.getColor("check");
        textButtonStyle.up = this.colors.getColor("front");
        textButtonStyle.down = this.colors.getColor("down");
        textButtonStyle.over = this.colors.getColor("select");
        textButtonStyle.disabled = this.colors.getColor("check");
        textButtonStyle.disabledFontColor = this.colors.getBasicColor("offFont");
        textButtonStyle.font = bitmapFont;
        textButtonStyle.fontColor = this.colors.getBasicColor(str);
    }

    private void initFont(UniversalFieldStyle universalFieldStyle, BitmapFont bitmapFont, String str, String str2) {
        universalFieldStyle.font = bitmapFont;
        if (str != null) {
            universalFieldStyle.fontColor = this.colors.getBasicColor(str);
        }
        if (str2 != null) {
            universalFieldStyle.offFontColor = this.colors.getBasicColor(str2);
        }
    }

    private void initTextField(UniversalFieldStyle universalFieldStyle) {
        universalFieldStyle.cursor = this.colors.getColor("2details");
        universalFieldStyle.selection = this.colors.getColor("select");
    }

    private void initBack(UniversalFieldStyle universalFieldStyle, String str, String str2, String str3, String str4) {
        universalFieldStyle.base = this.colors.getColor(str);
        universalFieldStyle.selected = this.colors.getColor(str2);
        universalFieldStyle.disabled = this.colors.getColor(str3);
        universalFieldStyle.over = this.colors.getColor(str4);
    }

    private void setLabelST(Label.LabelStyle labelStyle, BitmapFont bitmapFont, String str, boolean z) {
        if (z) {
            labelStyle.background = this.colors.getColor("front");
        }
        labelStyle.font = bitmapFont;
        labelStyle.fontColor = this.colors.getBasicColor(str);
    }

    public void create() {
        Gdx.input.setCatchKey(4, true);
        INIT_SETTINGS();
        currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        DEF_LOC_RU();
        this.batch = new SpriteBatch();
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        this.colors = new ColorSettings("Colors/" + settings.getValue("color") + ".bmp", "back", "base", "front", "check", "down", "select", "1details", "2details", "good", "normal", "bad", "mainFont", "secondaryFont", "offFont");
        FontGenerator fontGenerator = new FontGenerator("UbuntuMono", "ttf");
        fontSettings.main = fontGenerator.generate(this.colors.getBasicColor("mainFont"), percH(0.05f, 80));
        fontSettings.less = fontGenerator.generate(this.colors.getBasicColor("mainFont"), percH(0.035f));
        fontSettings.secondary = fontGenerator.generate(this.colors.getBasicColor("secondaryFont"), percH(0.025f));
        fontSettings.alert = fontGenerator.generate(this.colors.getBasicColor("bad"), percH(0.05f));
        this.containerFactory = new ContainerFactory(this);
        LOGGER = initLogger();
        Prototypes.init();
        load();
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = this.colors.getColor("back");
        scrollPaneStyle.vScroll = this.colors.getColor("1details");
        scrollPaneStyle.vScrollKnob = this.colors.getColor("2details");
        ScrollPaneFactory.register("main", scrollPaneStyle);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        setTextButtonST(textButtonStyle, fontSettings.main.regular, "mainFont");
        TextButtonFactory.register("BIG", textButtonStyle);
        TextButton.TextButtonStyle textButtonStyle2 = new TextButton.TextButtonStyle();
        setTextButtonST(textButtonStyle2, fontSettings.secondary.bold, "mainFont");
        TextButtonFactory.register("MEDIUM", textButtonStyle2);
        TextButton.TextButtonStyle textButtonStyle3 = new TextButton.TextButtonStyle();
        setTextButtonST(textButtonStyle3, fontSettings.less.bold, "mainFont");
        TextButtonFactory.register("SMALL", textButtonStyle3);
        TextButton.TextButtonStyle textButtonStyle4 = new TextButton.TextButtonStyle();
        setTextButtonST(textButtonStyle4, fontSettings.secondary.bold, "secondaryFont");
        TextButtonFactory.register("INFO", textButtonStyle4);
        TextButton.TextButtonStyle textButtonStyle5 = new TextButton.TextButtonStyle();
        setTextButtonST(textButtonStyle5, fontSettings.main.regular, "secondaryFont");
        textButtonStyle5.up = this.colors.getColor("bad");
        textButtonStyle5.down = this.colors.getColor("normal");
        TextButtonFactory.register("BAD", textButtonStyle5);
        TextButton.TextButtonStyle textButtonStyle6 = new TextButton.TextButtonStyle();
        setTextButtonST(textButtonStyle6, fontSettings.main.regular, "secondaryFont");
        textButtonStyle6.up = this.colors.getColor("good");
        textButtonStyle6.down = this.colors.getColor("normal");
        TextButtonFactory.register("GOOD", textButtonStyle6);
        TextButton.TextButtonStyle textButtonStyle7 = new TextButton.TextButtonStyle();
        textButtonStyle7.checked = this.colors.getColor("good");
        textButtonStyle7.over = this.colors.getColor("select");
        textButtonStyle7.up = this.colors.getColor("front");
        textButtonStyle7.disabled = this.colors.getColor("back");
        TextButtonFactory.register("FLAG", textButtonStyle7);
        TextButton.TextButtonStyle textButtonStyle8 = new TextButton.TextButtonStyle();
        textButtonStyle8.font = fontSettings.main.regular;
        textButtonStyle8.fontColor = this.colors.getBasicColor("mainFont");
        textButtonStyle8.up = this.colors.getColor("back");
        TextButtonFactory.register("SPACE", textButtonStyle8);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        setLabelST(labelStyle, fontSettings.main.regular, "mainFont", true);
        LabelFactory.register("BIG", labelStyle);
        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        setLabelST(labelStyle2, fontSettings.main.bold, "mainFont", true);
        labelStyle2.background = null;
        LabelFactory.register("BIG_BOLD", labelStyle2);
        Label.LabelStyle labelStyle3 = new Label.LabelStyle();
        setLabelST(labelStyle3, fontSettings.main.regular, "mainFont", false);
        LabelFactory.register("BIG_NO_B", labelStyle3);
        Label.LabelStyle labelStyle4 = new Label.LabelStyle();
        setLabelST(labelStyle4, fontSettings.secondary.regular, "mainFont", true);
        LabelFactory.register("MEDIUM", labelStyle4);
        Label.LabelStyle labelStyle5 = new Label.LabelStyle();
        setLabelST(labelStyle5, fontSettings.secondary.regular, "mainFont", false);
        LabelFactory.register("MEDIUM_NO_B", labelStyle5);
        Label.LabelStyle labelStyle6 = new Label.LabelStyle();
        setLabelST(labelStyle6, fontSettings.less.regular, "mainFont", true);
        LabelFactory.register("SMALL", labelStyle6);
        Label.LabelStyle labelStyle7 = new Label.LabelStyle();
        setLabelST(labelStyle7, fontSettings.less.regular, "mainFont", false);
        LabelFactory.register("SMALL_NO_B", labelStyle7);
        Label.LabelStyle labelStyle8 = new Label.LabelStyle();
        setLabelST(labelStyle8, fontSettings.secondary.regular, "secondaryFont", true);
        LabelFactory.register("INFO", labelStyle8);
        Label.LabelStyle labelStyle9 = new Label.LabelStyle();
        setLabelST(labelStyle9, fontSettings.secondary.regular, "secondaryFont", false);
        LabelFactory.register("INFO_NO_B", labelStyle9);
        Label.LabelStyle labelStyle10 = new Label.LabelStyle();
        labelStyle10.font = fontSettings.main.bold;
        labelStyle10.fontColor = this.colors.getBasicColor("bad");
        LabelFactory.register("BAD", labelStyle10);
        Label.LabelStyle labelStyle11 = new Label.LabelStyle();
        labelStyle11.font = fontSettings.main.bold;
        labelStyle11.fontColor = this.colors.getBasicColor("normal");
        LabelFactory.register("NORMAL", labelStyle11);
        Label.LabelStyle labelStyle12 = new Label.LabelStyle();
        labelStyle12.font = fontSettings.main.bold;
        labelStyle12.fontColor = this.colors.getBasicColor("good");
        LabelFactory.register("GOOD", labelStyle12);
        UniversalFieldStyle universalFieldStyle = new UniversalFieldStyle();
        initFont(universalFieldStyle, fontSettings.secondary.regular, "secondaryFont", "offFont");
        initBack(universalFieldStyle, "front", "down", "check", "select");
        initTextField(universalFieldStyle);
        this.inputFactory.register("main", universalFieldStyle);
        this.screens.put("main", new MainScreen(this));
        this.screens.put("select", new EditCategoryMenu(this));
        DayEditScreen.mode = false;
        this.screens.put("dayedit", new DayEditScreen(this));
        this.screens.put("listYN", new IfScreen(this));
        this.screens.put("subSelect", new SubjectListScreen(this));
        this.screens.put("newSubject", new SubjectEditScreen(this));
        this.screens.put("lessonEdit", new LessonEditScreen(this));
        this.screens.put("timetableEdit", new TimetableSelectScreen(this));
        this.screens.put("selectDate", new GetTimeScreen(this));
        this.screens.put("settings", new SettingsScreen(this));
        this.screens.put("weekSelect", new WeeksSelectionScreen(this));
        this.screens.put("colorSelect", new ColorSelectScreen(this));
        this.screens.put("subjectMenu", new RealSubjectScreen(this));
        this.screens.put("newTask", new NewTaskScreen(this));
        this.screens.put("taskList", new GlobalTaskList(this));
        this.screens.put("taskElement", new TaskElementEditor(this));
        DayEditScreen.mode = true;
        this.screens.put("weekDayEdit", new DayEditScreen(this));
        setScreen("main");
    }

    public ContainerFactory.BoxContainer<Button> makeFlag(String str) {
        ContainerFactory.BoxContainer<Button> box = this.containerFactory.getBox(TextButtonFactory.make(str, false), 1);
        box.setInPad(5.0f);
        box.setInBackground(this.colors.getColor("back"));
        return box;
    }

    public void setScreen(String str) {
        setScreen(this.screens.get(str));
        if (this.screen != null) {
            this.history.add((BasicScreen) getScreen());
            return;
        }
        throw new NullPointerException("Oh shit! Screen is null!");
    }

    public void back() {
        if (this.history.size() <= 1) {
            this.screen.hide();
            save(true);
            Gdx.app.exit();
            return;
        }
        ((BasicScreen) getScreen()).back();
        ArrayList<BasicScreen> arrayList = this.history;
        arrayList.remove(arrayList.size() - 1);
        ArrayList<BasicScreen> arrayList2 = this.history;
        setScreen(arrayList2.get(arrayList2.size() - 1));
    }

    public VariableSystem returnValues() {
        ArrayList<BasicScreen> arrayList = this.history;
        return arrayList.get(arrayList.size() - 2).vars;
    }

    public void SAVE_SETTINGS() {
        try {
            new ObjectOutputStream(new FileOutputStream(Gdx.files.local("settings.b").file())).writeObject(settings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void INIT_SETTINGS() {
        FileHandle local = Gdx.files.local("settings.b");
        if (local.exists() && !local.isDirectory()) {
            try {
                settings = (VariableSystem) new ObjectInputStream(new FileInputStream(local.file())).readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (ClassNotFoundException e3) {
                e3.printStackTrace();
            }
        } else if (local.isDirectory()) {
            local.delete();
        }
        if (settings == null) {
            settings = new VariableSystem();
        }
        settings.defaultValue("color", "pink");
    }

    public void forceBack() {
        if (this.history.size() <= 1) {
            this.screen.hide();
            save(true);
            Gdx.app.exit();
            return;
        }
        ((BasicScreen) getScreen()).back();
        ArrayList<BasicScreen> arrayList = this.history;
        arrayList.remove(arrayList.size() - 1);
        super.setScreen((Screen) null);
    }

    /* access modifiers changed from: package-private */
    public int percW(float f) {
        return (int) (((float) this.width) * f);
    }

    /* access modifiers changed from: package-private */
    public int percH(float f) {
        return (int) (((float) this.height) * f);
    }

    /* access modifiers changed from: package-private */
    public int percH(float f, int i) {
        return Math.min(percH(f), i);
    }

    private void save(boolean z) {
        try {
            DataAPI.save(DATAFILE, z);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
        }
    }

    private void load() {
        try {
            DataAPI.load(DATAFILE);
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
        }
    }

    public void pause() {
        super.pause();
        save(false);
    }

    public void resume() {
        super.resume();
    }

    public void dispose() {
        super.dispose();
        save(true);
    }

    private static Logger initLogger() {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream(Gdx.files.internal("log.cfg").file()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Logger.getLogger("Core");
    }

    public VariableSystem varsOf(String str) {
        return this.screens.get(str).vars;
    }

    public void render() {
        super.render();
        this._time += Gdx.graphics.getDeltaTime();
        this._time2 += Gdx.graphics.getDeltaTime();
        if (this._time > 60.0f) {
            currentDate.setTime(new Date());
            this._time = 0.0f;
        }
        if (this._time2 > 120.0f) {
            save(true);
            this._time2 = 0.0f;
        }
    }

    public static GregorianCalendar getCurrentDate() {
        return currentDate;
    }

    private static void DEF_LOC_RU() {
        Locale.register("M00", "Январь");
        Locale.register("M01", "Февраль");
        Locale.register("M02", "Март");
        Locale.register("M03", "Апрель");
        Locale.register("M04", "Май");
        Locale.register("M05", "Июнь");
        Locale.register("M06", "Июль");
        Locale.register("M07", "Август");
        Locale.register("M08", "Сентябрь");
        Locale.register("M09", "Октябрь");
        Locale.register("M10", "Ноябрь");
        Locale.register("M11", "Декабрь");
        Locale.register("D00", "ПН");
        Locale.register("D01", "ВТ");
        Locale.register("D02", "СР");
        Locale.register("D03", "ЧТ");
        Locale.register("D04", "ПТ");
        Locale.register("D05", "СБ");
        Locale.register("D06", "ВС");
        Locale.register("D10", "Понедельник");
        Locale.register("D11", "Вторник");
        Locale.register("D12", "Среда");
        Locale.register("D13", "Четверг");
        Locale.register("D14", "Пятница");
        Locale.register("D15", "Суббота");
        Locale.register("D16", "Воскресенье");
        Locale.register("blue", "Голубой");
        Locale.register("pink", "Розовый");
        Locale.register("gray", "Серый");
        Locale.register("light", "Светлые");
        Locale.register("0DM", "Точная дата");
        Locale.register("1DM", "Предмет");
    }
}
