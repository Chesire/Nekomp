@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekomp.feature.settings.ui

import app.cash.turbine.test
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.models.ImageQuality
import com.chesire.nekomp.core.preferences.models.Theme
import com.chesire.nekomp.core.preferences.models.TitleLanguage
import com.chesire.nekomp.feature.settings.core.LogoutExecutor
import com.chesire.nekomp.feature.settings.data.ApplicationVersionInfo
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

class SettingsViewModelTest : FunSpec({

    val applicationSettings = mock<ApplicationSettings>(MockMode.autofill)
    val applicationVersionInfo = mock<ApplicationVersionInfo> {
        every { versionName } returns "0.0.0"
        every { versionCode } returns 0
    }
    val logout = mock<LogoutExecutor>(MockMode.autoUnit)
    lateinit var viewModel: SettingsViewModel

    coroutineTestScope = true

    beforeTest {
        resetCalls()
        resetAnswers()
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel = SettingsViewModel(
            applicationSettings,
            applicationVersionInfo,
            logout
        )
    }

    test("When initialize, Then UI is default state") {

    }

    test("When onThemeClick, Then theme bottom sheet is shown") {
        every { applicationSettings.theme } returns flowOf(Theme.System)

        viewModel.uiState.test {
            viewModel.execute(ViewAction.ThemeClick)

            awaitItem().bottomSheet.shouldBeInstanceOf<SettingsBottomSheet.ThemeBottomSheet>()
        }
    }

    test("When onThemeChosen, Then theme is updated in the settings") {
        every { applicationSettings.theme } returns flowOf(Theme.System)

        viewModel.execute(ViewAction.ThemeChosen(Theme.Dark))

        verifySuspend { applicationSettings.updateTheme(Theme.Dark) }
    }

    test("When onThemeChosen, Then bottom sheet is reset") {
        every { applicationSettings.theme } returns flowOf(Theme.System)

        viewModel.uiState.test {
            viewModel.execute(ViewAction.ThemeClick)
            awaitItem().bottomSheet.shouldBeInstanceOf<SettingsBottomSheet.ThemeBottomSheet>()

            viewModel.execute(ViewAction.ThemeChosen(Theme.Dark))

            awaitItem().bottomSheet.shouldBeNull()
        }
    }

    test("When onTitleLanguageClick, Then title bottom sheet is shown") {
        every { applicationSettings.titleLanguage } returns flowOf(TitleLanguage.CJK)

        viewModel.uiState.test {
            viewModel.execute(ViewAction.TitleLanguageClick)

            awaitItem().bottomSheet.shouldBeInstanceOf<SettingsBottomSheet.TitleLanguageBottomSheet>()
        }
    }

    test("When onTitleLanguageChosen, Then title is updated in the settings") {
        every { applicationSettings.titleLanguage } returns flowOf(TitleLanguage.CJK)

        viewModel.execute(ViewAction.TitleLanguageChosen(TitleLanguage.Canonical))

        verifySuspend { applicationSettings.updateTitleLanguage(TitleLanguage.Canonical) }
    }

    test("When onTitleLanguageChosen, Then bottom sheet is reset") {
        every { applicationSettings.titleLanguage } returns flowOf(TitleLanguage.CJK)

        viewModel.uiState.test {
            viewModel.execute(ViewAction.TitleLanguageClick)
            awaitItem().bottomSheet.shouldBeInstanceOf<SettingsBottomSheet.TitleLanguageBottomSheet>()

            viewModel.execute(ViewAction.TitleLanguageChosen(TitleLanguage.Romaji))

            awaitItem().bottomSheet.shouldBeNull()
        }
    }

    test("When onImageQualityClick, Then quality bottom sheet is shown") {
        every { applicationSettings.imageQuality } returns flowOf(ImageQuality.High)

        viewModel.uiState.test {
            viewModel.execute(ViewAction.ImageQualityClick)

            awaitItem().bottomSheet.shouldBeInstanceOf<SettingsBottomSheet.ImageQualityBottomSheet>()
        }
    }

    test("When onImageQualityChosen, Then quality is updated in the settings") {
        every { applicationSettings.imageQuality } returns flowOf(ImageQuality.High)

        viewModel.execute(ViewAction.ImageQualityChosen(ImageQuality.Highest))

        verifySuspend { applicationSettings.updateImageQuality(ImageQuality.Highest) }
    }

    test("When onImageQualityChosen, Then bottom sheet is reset") {
        every { applicationSettings.imageQuality } returns flowOf(ImageQuality.High)

        viewModel.uiState.test {
            viewModel.execute(ViewAction.ImageQualityClick)
            awaitItem().bottomSheet.shouldBeInstanceOf<SettingsBottomSheet.ImageQualityBottomSheet>()

            viewModel.execute(ViewAction.ImageQualityChosen(ImageQuality.Highest))

            awaitItem().bottomSheet.shouldBeNull()
        }
    }

    test("When onRateChanged, Then rate setting is updated in settings") {
        every { applicationSettings.rateOnFinish } returns flowOf(true)

        viewModel.execute(ViewAction.RateChanged)

        verifySuspend { applicationSettings.updateRateOnFinish(true) }
    }

    test("When onLogoutClick, Then logout is executed") {
        viewModel.execute(ViewAction.LogoutClick)

        verifySuspend { logout.execute() }
    }

    test("When onObservedViewEvent, Then viewEvent is reset") {
        viewModel.uiState.test {
            viewModel.execute(ViewAction.LogoutClick)
            awaitItem().viewEvent.shouldBe(ViewEvent.LoggedOut)

            viewModel.execute(ViewAction.ObservedViewEvent)
            awaitItem().viewEvent.shouldBeNull()
        }
    }
})
