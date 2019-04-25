package org.api.script.impl.mission.tutorial_island_mission.data.args;

import com.beust.jcommander.Parameter;
import org.rspeer.runetek.api.movement.position.Position;

public class Args {

    @Parameter(names = "-drop_items", arity = 1)
    public boolean drop_items;

    @Parameter(names = "-bank_items", arity = 1)
    public boolean bank_items;

    @Parameter(names = "-walk_position", converter = PositionConverter.class)
    public Position walk_position;

    @Parameter(names = "-hide_roofs", arity = 1)
    public boolean hide_roofs;

    @Parameter(names = "-set_audio")
    public int set_audio;

    @Parameter(names = "-set_brightness")
    public int set_brightness;

    @Parameter(names = "-set_zoom")
    public int set_zoom;

    @Parameter(names = "-stay_logged_in", arity = 1)
    public boolean stay_logged_in;

    @Parameter(names = "-end_on_completion")
    public boolean end_on_completion;

    @Parameter(names = "-account_list")
    public String account_list;

    @Parameter(names = "-accounts_to_create")
    public int accounts_to_create;

    @Parameter(names = "-two_captcha_api_key")
    public String two_captcha_api_key;

    @Parameter(names = "-proxy_list")
    public String proxy_list;

    @Parameter(names = "-accounts_per_proxy")
    public int accounts_per_proxy;

}

