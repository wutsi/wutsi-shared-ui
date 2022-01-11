package com.wutsi.application.shared.service

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.account.dto.Account
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TogglesProviderTest {
    companion object {
        const val USER_ID = 1L
    }

    private lateinit var userProvider: SecurityContext

    @BeforeEach
    fun setUp() {
        userProvider = mock()
        doReturn(USER_ID).whenever(userProvider).currentUserId()
    }

    @Test
    fun `payment=ON - payment enabled for business account`() {
        // GIVEN
        val toggles = createToggleForPayment(true)
        val account = Account(business = true)

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        assertTrue(service.isPaymentEnabled(account))
    }

    @Test
    fun `payment=ON - payment disabled for non-business account`() {
        // GIVEN
        val toggles = createToggleForPayment(true)
        val account = Account(business = false)

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        kotlin.test.assertFalse(service.isPaymentEnabled(account))
    }

    @Test
    fun `payment=OFF - payment disable for business account`() {
        // GIVEN
        val toggles = createToggleForPayment(false, listOf(1111, 2222))
        val account = Account(business = true)

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        kotlin.test.assertFalse(service.isPaymentEnabled(account))
    }

    @Test
    fun `payment=OFF - payment enabled for tester business account`() {
        // GIVEN
        val toggles = createToggleForPayment(false, listOf(USER_ID, 2, 3))
        val account = Account(id = USER_ID, business = true)

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        assertTrue(service.isPaymentEnabled(account))
    }

    @Test
    fun `payment=OFF - payment disabled for tester non-business account`() {
        // GIVEN
        val toggles = createToggleForPayment(false, listOf(USER_ID, 2, 3))
        val account = Account(id = USER_ID, business = false)

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        kotlin.test.assertFalse(service.isPaymentEnabled(account))
    }

    @Test
    fun `scan=ON - payment enabled`() {
        // GIVEN
        val toggles = createToggleForScan(true)
        val account = Account()

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        assertTrue(service.isScanEnabled(account))
    }

    @Test
    fun `scan=OFF - payment disabled`() {
        // GIVEN
        val toggles = createToggleForScan(false)
        val account = Account()

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        kotlin.test.assertFalse(service.isScanEnabled(account))
    }

    @Test
    fun `scan=OFF - payment enabled for tester`() {
        // GIVEN
        val toggles = createToggleForScan(false, listOf(USER_ID, 2, 3))
        val account = Account(USER_ID)

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        assertTrue(service.isScanEnabled(account))
    }

    @Test
    fun `logout=ON - logout enabled`() {
        // GIVEN
        val toggles = createToggleForLogout(true)

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        assertTrue(service.isLogoutEnabled())
    }

    @Test
    fun `logout=OFF - logout disabled`() {
        // GIVEN
        val toggles = createToggleForLogout(false)

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        kotlin.test.assertFalse(service.isLogoutEnabled())
    }

    @Test
    fun `logout=OFF and user is tester - logout enabled`() {
        // GIVEN
        val toggles = createToggleForLogout(false, listOf(USER_ID))

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        assertTrue(service.isLogoutEnabled())
    }

    @Test
    fun `sendSMS=OFF deature enabled`() {
        // GIVEN
        val toggles = createToggleForLogout(false, listOf(USER_ID))

        // WHEN
        val service = createToggleProvider(toggles)

        // THEN
        assertTrue(service.isLogoutEnabled())
    }

    private fun createToggleForLogout(value: Boolean, testerUserIds: List<Long> = emptyList()): Toggles {
        val toggles = Toggles()
        toggles.logout = value
        toggles.testerUserIds = testerUserIds
        return toggles
    }

    private fun createToggleForPayment(value: Boolean, testerUserIds: List<Long> = emptyList()): Toggles {
        val toggles = Toggles()
        toggles.payment = value
        toggles.testerUserIds = testerUserIds
        return toggles
    }

    private fun createToggleForScan(value: Boolean, testerUserIds: List<Long> = emptyList()): Toggles {
        val toggles = Toggles()
        toggles.scan = value
        toggles.testerUserIds = testerUserIds
        return toggles
    }

    private fun createToggleProvider(toggles: Toggles) = TogglesProvider(
        toggles, userProvider
    )
}
