package com.backbase.kalah.web;

import com.backbase.kalah.game.GameCreator;
import com.backbase.kalah.game.GameManager;
import com.backbase.kalah.game.GameMockery;
import io.vavr.control.Try;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

import static io.vavr.control.Try.success;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @MockBean
    private GameCreator gameCreator;
    @MockBean
    private GameManager gameManager;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class GameCreation {

        @Test
        void shouldReturnLinkToGameWhenOK() throws Exception {
            final var uuid = UUID.randomUUID();
            when(gameCreator.createGame()).thenReturn(success(uuid));

            mockMvc.perform(post("/games"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(uuid.toString())))
                    .andExpect(jsonPath("$.uri", is("http://localhost:8080/games/" + uuid)));

            verify(gameManager, never()).fetchGame(any());
            verify(gameManager, never()).makeAMove(any(), any());
        }

        @Test
        void shouldReturn500WhenErrorWhileCreating() throws Exception {
            final var uuid = UUID.randomUUID();
            when(gameCreator.createGame()).thenReturn(Try.failure(new IllegalStateException("something went wrong")));

            mockMvc.perform(post("/games"))
                    .andExpect(status().isInternalServerError());

            verify(gameManager, never()).fetchGame(any());
            verify(gameManager, never()).makeAMove(any(), any());
        }

    }

    @Nested
    class FetchingGame {

        @Test
        void shouldReturn200WhenGameWentFound() throws Exception {
            //given
            final var uuid = UUID.fromString("ba1ff57c-a25c-404e-819b-a6175509d9a4");
            final var expectedResult = loadJson("new-game.json");

            //when
            when(gameManager.fetchGame(uuid)).thenReturn(Optional.of(GameMockery.gameInPlay(uuid)));
            final var response = mockMvc.perform(get("/games/" + uuid))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            //then
            JSONAssert.assertEquals(expectedResult, response, true);
        }

        @Test
        void shouldReturn404WhenGameNotFound() throws Exception {
            //given
            final var uuid = UUID.fromString("ba1ff57c-a25c-404e-819b-a6175509d9a4");

            //when
            when(gameManager.fetchGame(uuid)).thenReturn(Optional.empty());
            mockMvc.perform(get("/games/" + uuid))
                    .andExpect(status().isNotFound());
        }
    }


    private String loadJson(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName)
                .getFile());
        return Files.readString(file.toPath());
    }
}
