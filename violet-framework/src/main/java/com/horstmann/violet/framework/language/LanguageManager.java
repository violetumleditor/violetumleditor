package com.horstmann.violet.framework.language;

import com.horstmann.violet.framework.userpreferences.UserPreferencesService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;

public class LanguageManager
{

    private List<Language> languages = new ArrayList<Language>();
    private static final String resourceDir = "com/horstmann/violet/framework/language/";

    public LanguageManager()
    {
        BeanInjector.getInjector().inject(this);
        loadAvailableLanguage();
    }

    public List<Language> getLanguages()
    {
        return languages;
    }

    public void setPreferedLanguage(String language)
    {
        this.userPreferencesServices.setPreferedLanguage(language);
    }

    public String getPreferedLanguage()
    {
        return this.userPreferencesServices.getPreferedLanguage();
    }

    public void loadAvailableLanguage()
    {
        String[] languages = Locale.getISOLanguages();
        for (String countryCode : languages)
        {
            String path = "Language_" + countryCode + ".properties";
            URL file = ClassLoader.getSystemResource(resourceDir + path);

            if (file != null)
            {
                Locale locale = new Locale(countryCode);
                String languageName = locale.getDisplayLanguage(locale);
                this.languages.add(new Language(countryCode, languageName));
            }
        }
    }

    public void applyPreferedLanguage()
    {
        Locale locale = new Locale(getPreferedLanguage());
        Locale.setDefault(locale);
    }

    @InjectedBean
    private UserPreferencesService userPreferencesServices;
}
