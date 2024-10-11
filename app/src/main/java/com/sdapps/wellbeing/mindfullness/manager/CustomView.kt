package com.sdapps.wellbeing.mindfullness.manager

import com.sdapps.wellbeing.mindfullness.data.MenuBO

interface CustomView {
    fun handleCardClick(moduleName: String, menuBO: MenuBO)
}