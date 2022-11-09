package co.bearus.cosmoapi

import co.bearus.cosmoapi.domain.UserRepository
import co.bearus.cosmoapi.domain.cash.ChargeType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CosmoApiApplicationTests @Autowired constructor(
    private val userRepository: UserRepository
) {
    @Test
    fun contextLoads() {
        val user = this.userRepository.findByUsername("ChuYong")
        user.addCash(1000, ChargeType.SYSTEM)
        user.addWarning(1)
        user.resetCultureLandFailureCount()

        this.userRepository.save(user)
    }
}
