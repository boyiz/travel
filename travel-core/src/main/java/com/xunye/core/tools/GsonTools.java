package com.xunye.core.tools;//package com.boyiz.jtar.core.tools;

import com.google.gson.Gson;

public class GsonTools {

    private static volatile Gson gson = null;

    public static Gson getGson() {
        if (gson == null) {
            synchronized (GsonTools.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

}
