package com.superbapps.ui.view;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import kaesdingeling.hybridmenu.components.ColorPicker;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.superbapps.ui.MenuUI;
import com.vaadin.server.Responsive;
import kaesdingeling.hybridmenu.builder.NotificationBuilder;
import kaesdingeling.hybridmenu.data.DesignItem;
import kaesdingeling.hybridmenu.data.enums.ENotificationPriority;

import javax.annotation.PostConstruct;

@SpringView
public class ThemeBuilderPage extends VerticalLayout implements View {

    private DesignItem designItem;
    private boolean lockForLoad = true;

    /* Colors */
    private final FormLayout colorsForm = buildForm("Colors");
    private final ColorPicker darkColor = buildPicker(colorsForm, "Dark-Color");
    private final ColorPicker whiteColor = buildPicker(colorsForm, "White-Color");

    /* Content-Background */
    private final FormLayout contentBackgroundForm = buildForm("Content-Background");
    private final ColorPicker contentBackground = buildPicker(contentBackgroundForm, "Content-Background");

    /* Notifications */
    private final FormLayout notificationForm = buildForm("Notifications");
    private final ColorPicker notificationPrioLow = buildPicker(notificationForm, "Notification-Prio-Low");
    private final ColorPicker notificationPrioMedium = buildPicker(notificationForm, "Notification-Prio-Medium");
    private final ColorPicker notificationPrioHigh = buildPicker(notificationForm, "Notification-Prio-High");
    private final Slider notificationBorderColorOpacity = buildSlider(notificationForm, "Notification-Border-Color-Opacity");
    private final Slider notificationShadow = buildSlider(notificationForm, "Notification-Shadow");
    private final ColorPicker notificationCloseButtonHover = buildPicker(notificationForm, "Notification-Close-Button-Hover");
    private final Slider notificationCloseButtonOpacity = buildSlider(notificationForm, "Notification-Close-Button-Opacity");

    /* Left-Menu */
    private final FormLayout leftMenuForm = buildForm("Left-Menu");
    private final ColorPicker leftMenuBackground = buildPicker(leftMenuForm, "Left-Menu-Background");
    private final ColorPicker leftMenuShadow = buildPicker(leftMenuForm, "Left-Menu-Shadow");
    private final Slider leftMenuShadowOpacity = buildSlider(leftMenuForm, "Left-Menu-Shadow-Opacity");
    private final Slider leftMenuButtonOpacity = buildSlider(leftMenuForm, "Left-Menu-Button-Opacity");
    private final ColorPicker leftMenuButtonHover = buildPicker(leftMenuForm, "Left-Menu-Button-Hover");
    private final ColorPicker leftMenuButtonBorderHover = buildPicker(leftMenuForm, "Left-Menu-Button-Border-Hover");
    private final ColorPicker leftMenuButtonActive = buildPicker(leftMenuForm, "Left-Menu-Button-Active");
    private final ColorPicker leftMenuButtonBorderActive = buildPicker(leftMenuForm, "Left-Menu-Button-Border-Active");
    private final ColorPicker leftMenuSubMenuBorder = buildPicker(leftMenuForm, "Left-Menu-Sub-Menu-Border");
    private final ColorPicker leftMenuButtonTooltip = buildPicker(leftMenuForm, "Left-Menu-Button-Tooltip");

    /* Top-Menu */
    private final FormLayout topMenuForm = buildForm("Top-Menu");
    private final ColorPicker topMenuBackground = buildPicker(topMenuForm, "Top-Menu-Background");
    private final ColorPicker topMenuShadow = buildPicker(topMenuForm, "Top-Menu-Shadow-Two");
    private final Slider topMenuShadowOpacity = buildSlider(topMenuForm, "Top-Menu-Shadow-Opacity");
    private final ColorPicker topMenuButtonHover = buildPicker(topMenuForm, "Top-Menu-Button-Hover");
    private final ColorPicker topMenuButtonActive = buildPicker(topMenuForm, "Top-Menu-Button-Active");
    private final ColorPicker topMenuButtonBorder = buildPicker(topMenuForm, "Top-Menu-Button-Border");
    private final ColorPicker topMenuButtonTooltip = buildPicker(topMenuForm, "Top-Menu-Button-Tooltip");

    private final TextArea jsonOutput = new TextArea("JSON-Output");

    private final Button importButton = new Button("Import JSON");

    @PostConstruct
    void init() {
        Responsive.makeResponsive(this);

        Label title = new Label();
        title.setCaption("Theme Builder");
        title.setValue("Here you have the possibility to create your own colour scheme.");

        jsonOutput.setWidth("100%");
        jsonOutput.setHeight("500px");

        addComponents(title, colorsForm, contentBackgroundForm, notificationForm, leftMenuForm, topMenuForm, jsonOutput, importButton);

        buildNewObject();
        load();

        importButton.addClickListener(e -> {
            lockForLoad = true;
            try {
                designItem = new Gson().fromJson(jsonOutput.getValue(), DesignItem.class);
                NotificationBuilder.get(((MenuUI) UI.getCurrent()).getHybridMenu().getNotificationCenter())
                        .withCaption("Import successful")
                        .withDescription("The design adapts itself automatically")
                        .withPriority(ENotificationPriority.MEDIUM)
                        .withIcon(VaadinIcons.INFO)
                        .withCloseByHide()
                        .build();
            } catch (JsonSyntaxException e2) {
                NotificationBuilder.get(((MenuUI) UI.getCurrent()).getHybridMenu().getNotificationCenter())
                        .withCaption("Import failed")
                        .withDescription("The entry was rejected")
                        .withPriority(ENotificationPriority.MEDIUM)
                        .withIcon(VaadinIcons.INFO)
                        .withCloseByHide()
                        .build();
                buildNewObject();
            } finally {
                load();
                update();
                lockForLoad = false;
            }
        });

        update();
        lockForLoad = false;
    }

    private void load() {
        /* Colors */
        darkColor.setValue(designItem.getDarkColor());
        whiteColor.setValue(designItem.getWhiteColor());

        /* Content-Background */
        contentBackground.setValue(designItem.getContentBackground());

        /* Notifications */
        notificationPrioLow.setValue(designItem.getNotificationPrioLowBackground());
        notificationPrioMedium.setValue(designItem.getNotificationPrioMediumBackground());
        notificationPrioHigh.setValue(designItem.getNotificationPrioHighBackground());
        notificationBorderColorOpacity.setValue(designItem.getNotificationBorderColorOpacity());
        notificationShadow.setValue(designItem.getNotificationShadow());
        notificationCloseButtonHover.setValue(designItem.getNotificationCloseButtonHover());
        notificationCloseButtonOpacity.setValue(designItem.getNotificationCloseButtonOpacity());

        /* Left-Menu */
        leftMenuBackground.setValue(designItem.getMenuLeftBackground());
        leftMenuShadow.setValue(designItem.getMenuLeftShadow());
        leftMenuShadowOpacity.setValue(designItem.getMenuLeftShadowOpacity());
        leftMenuButtonOpacity.setValue(designItem.getMenuLeftButtonOpacity());
        leftMenuButtonHover.setValue(designItem.getMenuLeftButtonHover());
        leftMenuButtonBorderHover.setValue(designItem.getMenuLeftButtonBorderHover());
        leftMenuButtonActive.setValue(designItem.getMenuLeftButtonActive());
        leftMenuButtonBorderActive.setValue(designItem.getMenuLeftButtonBorderActive());
        leftMenuSubMenuBorder.setValue(designItem.getMenuLeftSubMenuBorder());
        leftMenuButtonTooltip.setValue(designItem.getMenuLeftButtonTooltip());

        /* Top-Menu */
        topMenuBackground.setValue(designItem.getMenuTopBackground());
        topMenuShadow.setValue(designItem.getMenuTopShadow());
        topMenuShadowOpacity.setValue(designItem.getMenuTopShadowOpacity());
        topMenuButtonHover.setValue(designItem.getMenuTopButtonHover());
        topMenuButtonActive.setValue(designItem.getMenuTopButtonActive());
        topMenuButtonBorder.setValue(designItem.getMenuTopButtonBorder());
        topMenuButtonTooltip.setValue(designItem.getMenuTopButtonTooltip());
    }

    private void write() {
        if (!lockForLoad) {
            /* Colors */
            designItem.setDarkColor(darkColor.getValue());
            designItem.setWhiteColor(whiteColor.getValue());

            /* Content-Background */
            designItem.setContentBackground(contentBackground.getValue());

            /* Notifications */
            designItem.setNotificationPrioLowBackground(notificationPrioLow.getValue());
            designItem.setNotificationPrioMediumBackground(notificationPrioMedium.getValue());
            designItem.setNotificationPrioHighBackground(notificationPrioHigh.getValue());
            designItem.setNotificationBorderColorOpacity(notificationBorderColorOpacity.getValue());
            designItem.setNotificationShadow(notificationShadow.getValue());
            designItem.setNotificationCloseButtonHover(notificationCloseButtonHover.getValue());
            designItem.setNotificationCloseButtonOpacity(notificationCloseButtonOpacity.getValue());

            /* Left-Menu */
            designItem.setMenuLeftBackground(leftMenuBackground.getValue());
            designItem.setMenuLeftShadow(leftMenuShadow.getValue());
            designItem.setMenuLeftShadowOpacity(leftMenuShadowOpacity.getValue());
            designItem.setMenuLeftButtonOpacity(leftMenuButtonOpacity.getValue());
            designItem.setMenuLeftButtonHover(leftMenuButtonHover.getValue());
            designItem.setMenuLeftButtonBorderHover(leftMenuButtonBorderHover.getValue());
            designItem.setMenuLeftButtonActive(leftMenuButtonActive.getValue());
            designItem.setMenuLeftButtonBorderActive(leftMenuButtonBorderActive.getValue());
            designItem.setMenuLeftSubMenuBorder(leftMenuSubMenuBorder.getValue());
            designItem.setMenuTopButtonTooltip(leftMenuButtonTooltip.getValue());

            /* Top-Menu */
            designItem.setMenuTopBackground(topMenuBackground.getValue());
            designItem.setMenuTopShadow(topMenuShadow.getValue());
            designItem.setMenuTopShadowOpacity(topMenuShadowOpacity.getValue());
            designItem.setMenuTopButtonHover(topMenuButtonHover.getValue());
            designItem.setMenuTopButtonActive(topMenuButtonActive.getValue());
            designItem.setMenuTopButtonBorder(topMenuButtonBorder.getValue());
            designItem.setMenuTopButtonTooltip(topMenuButtonTooltip.getValue());
        }
    }

    private void update() {
        write();
        ((MenuUI) UI.getCurrent()).getHybridMenu().switchTheme(designItem);
        jsonOutput.setValue(new Gson().toJson(designItem));
    }

    private void buildNewObject() {
        designItem = new DesignItem();

        /* Colors */
        designItem.setDarkColor(new Color(66, 66, 66));
        designItem.setWhiteColor(new Color(245, 245, 245));

        /* Content-Background */
        designItem.setContentBackground(new Color(245, 245, 245));

        /* Notifications */
        designItem.setNotificationPrioLowBackground(new Color(224, 224, 224));
        designItem.setNotificationPrioMediumBackground(new Color(0, 96, 100));
        designItem.setNotificationPrioHighBackground(new Color(191, 54, 12));
        designItem.setNotificationBorderColorOpacity(15.0);
        designItem.setNotificationShadow(70.0);
        designItem.setNotificationCloseButtonHover(new Color(222, 30, 30));
        designItem.setNotificationCloseButtonOpacity(50.0);

        /* Left-Menu */
        designItem.setMenuLeftBackground(new Color(238, 238, 238));
        designItem.setMenuLeftShadow(new Color(189, 189, 189));
        designItem.setMenuLeftShadowOpacity(40.0);
        designItem.setMenuLeftButtonOpacity(50.0);
        designItem.setMenuLeftButtonHover(new Color(224, 224, 224));
        designItem.setMenuLeftButtonBorderHover(new Color(25, 118, 210));
        designItem.setMenuLeftButtonActive(new Color(245, 245, 245));
        designItem.setMenuLeftButtonBorderActive(new Color(51, 105, 30));
        designItem.setMenuLeftSubMenuBorder(new Color(224, 224, 224));
        designItem.setMenuTopButtonTooltip(new Color(25, 118, 210));

        /* Top-Menu */
        designItem.setMenuTopBackground(new Color(224, 224, 224));
        designItem.setMenuTopShadow(new Color(189, 189, 189));
        designItem.setMenuTopShadowOpacity(40.0);
        designItem.setMenuTopButtonHover(new Color(51, 105, 30));
        designItem.setMenuTopButtonActive(new Color(46, 125, 50));
        designItem.setMenuTopButtonBorder(new Color(100, 221, 23));
        designItem.setMenuTopButtonTooltip(new Color(25, 118, 210));
    }

    private FormLayout buildForm(String caption) {
        FormLayout formLayout = new FormLayout();
        formLayout.setCaption(caption);
        return formLayout;
    }

    private ColorPicker buildPicker(Layout layout, String caption) {
        HorizontalLayout content = new HorizontalLayout();
        ColorPicker colorPicker = new ColorPicker(true);
        content.setCaption(caption);
        colorPicker.addValueChangeListener(e -> {
            update();
        });
        content.addComponent(colorPicker);
        layout.addComponent(content);
        return colorPicker;
    }

    private Slider buildSlider(Layout layout, String caption) {
        HorizontalLayout content = new HorizontalLayout();
        Slider slider = new Slider();
        Label hexCode = new Label();
        content.setCaption(caption);
        slider.setMin(0.0);
        slider.setMax(100.0);
        slider.addStyleName("ticks");
        slider.setWidth("300px");
        slider.addValueChangeListener(e -> {
            hexCode.setValue(String.valueOf((slider.getValue()).intValue()) + " %");
            update();
        });
        hexCode.setValue(String.valueOf((slider.getValue()).intValue()) + " %");
        content.addComponents(slider, hexCode);
        layout.addComponent(content);
        return slider;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
