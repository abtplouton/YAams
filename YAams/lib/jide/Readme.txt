The usage, dependency, redistributable status of each jar file under lib folder 

--- redistributable jars -----

    jide-common.jar
        the common jar used by all other JIDE products.
        This is redistributable if you purchased any of JIDE products.

    jide-dock.jar
        the jar for JIDE Docking Framework. It depends on jide-common.jar.
        This is redistributable if you purchased JIDE Docking Framework product.

    jide-components.jar
        the jar for JIDE Components. It depends on jide-common.jar.
        This is redistributable if you purchased JIDE Components product.

    jide-grids.jar
        the jar for JIDE Grids. It depends on jide-common.jar.
        This is redistributable if you purchased JIDE Grids product.

    jide-shortcut.jar
        the jar for JIDE Shortcut Editor. It depends on jide-common.jar, jide-components.jar and jide-grids.jar.
        This is redistributable if you purchased JIDE Shortcut Editor product.

    jide-editor.jar
        the jar for JIDE Code Editor. It depends on jide-common.jar jide-grids.jar jide-components.jar and jide-shortcut.jar.
        This is redistributable if you purchased JIDE Code Editor product.

    jide-charts.jar
        the jar for JIDE Charts. It depends on jide-common.jar, commons-math-1.2.jar, servlet-api.jar and batik-awt-util.jar.
        This is redistributable if you purchased JIDE Charts product.

    jide-synthetica.jar
        the jar contains a few extra classes to support Synthetica L&F. If you use Synthetica L&f in your application,
        you should include this jar in your class path. Otherwise no need to include.

    jide-properties.jar
        the jar contains all the properties files (localized strings) for different languages. If your application
        will run on different locales, you should include this jar file in the class path. Please note, this jar file
        contains as many as 48 languages for some of the properties file. So feel free to edit this jar to remove
        those properties files that your application won't need to reduce the jar file size.

-- For JIDE Charts only --
    commons-math-1.2.jar (http://commons.apache.org/math/, Apache license, version 1.2)
      3rd party jars used by JIDE Charts.
      The common-math-1.2.jar is only used by classes from com.jidesoft.chart.fit package.
