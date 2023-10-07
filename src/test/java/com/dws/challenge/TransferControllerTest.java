package com.dws.challenge;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class TransferControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private AccountsRepository accountsRepository;

  @MockBean
  private NotificationService notificationService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  void prepareMockMvc() {
    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

    // Reset the existing accounts before each test.
    accountsRepository.clearAccounts();
  }

  @Test
  void createAccount() throws Exception {

    //given
    accountsRepository.createAccount(new Account("1", BigDecimal.TEN, 0));
    accountsRepository.createAccount(new Account("2", BigDecimal.TEN, 0));
    //when
    this.mockMvc.perform(post("/v1/transfer").contentType(MediaType.APPLICATION_JSON)
        .content("{\"accountFromId\":\"1\",\"accountToId\":\"2\",\"amount\":10}")).andExpect(status().isOk());
    //then
    assertThat(accountsRepository.getAccount("1")).isNotEmpty();
    assertThat(accountsRepository.getAccount("1").get().getVersion()).isEqualTo(1);
    assertThat(accountsRepository.getAccount("1").get().getBalance()).isEqualTo(new BigDecimal(0));

    assertThat(accountsRepository.getAccount("2")).isNotEmpty();
    assertThat(accountsRepository.getAccount("2").get().getVersion()).isEqualTo(1);
    assertThat(accountsRepository.getAccount("2").get().getBalance()).isEqualTo(new BigDecimal(20));
  }

  @Test
  void createAccount_accountNotFound() throws Exception {
    //given
    accountsRepository.createAccount(new Account("1", BigDecimal.TEN, 0));
    accountsRepository.createAccount(new Account("2", BigDecimal.TEN, 0));
    //when
    this.mockMvc.perform(post("/v1/transfer").contentType(MediaType.APPLICATION_JSON)
        .content("{\"accountFromId\":\"1\",\"accountToId\":\"3\",\"amount\":10}")).andExpect(status().isBadRequest());
    //then
    assertThat(accountsRepository.getAccount("1")).isNotEmpty();
    assertThat(accountsRepository.getAccount("1").get().getVersion()).isEqualTo(0);
    assertThat(accountsRepository.getAccount("1").get().getBalance()).isEqualTo(new BigDecimal(10));

    assertThat(accountsRepository.getAccount("2")).isNotEmpty();
    assertThat(accountsRepository.getAccount("2").get().getVersion()).isEqualTo(0);
    assertThat(accountsRepository.getAccount("2").get().getBalance()).isEqualTo(new BigDecimal(10));
  }

  @Test
  void createAccount_amountNotValid() throws Exception {
    //given
    accountsRepository.createAccount(new Account("1", BigDecimal.TEN, 0));
    accountsRepository.createAccount(new Account("2", BigDecimal.TEN, 0));
    //when
    this.mockMvc.perform(post("/v1/transfer").contentType(MediaType.APPLICATION_JSON)
        .content("{\"accountFromId\":\"1\",\"accountToId\":\"2\",\"amount\":40}")).andExpect(status().isBadRequest());
    //then
    assertThat(accountsRepository.getAccount("1")).isNotEmpty();
    assertThat(accountsRepository.getAccount("1").get().getVersion()).isEqualTo(0);
    assertThat(accountsRepository.getAccount("1").get().getBalance()).isEqualTo(new BigDecimal(10));

    assertThat(accountsRepository.getAccount("2")).isNotEmpty();
    assertThat(accountsRepository.getAccount("2").get().getVersion()).isEqualTo(0);
    assertThat(accountsRepository.getAccount("2").get().getBalance()).isEqualTo(new BigDecimal(10));
  }

}
