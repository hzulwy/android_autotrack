package com.auto.track.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class SilveryAutoTrackPlugin implements Plugin<Project>{

    SilveryTrackExtension extension = project.extensions.create("silveryAutoTrack", SilveryTrackExtension)

    @Override
    void apply(Project project) {
        AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new SilveryAppTransform(project, extension))
    }
}