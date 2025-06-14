package ru.netology

import org.junit.Before
import org.junit.*
import org.junit.Assert.*

class WallServiceTest {
    private lateinit var service: WallService

    @Before
    fun setUp() {
        service = WallService()
        service.clear()
    }

    // ========== Существующие тесты ==========

    @Test
    fun testAddPost_IdIsAssigned() {
        val post = Post(
            id = 0,
            ownerId = 100,
            fromId = 100,
            date = System.currentTimeMillis() / 1000,
            text = "Тест добавления поста"
        )

        val addedPost = service.add(post)

        assertNotEquals(0, addedPost.id)
    }

    @Test
    fun testUpdateExistingPost() {
        val originalPost = service.add(
            Post(
                id = 0,
                ownerId = 100,
                fromId = 100,
                date = System.currentTimeMillis() / 1000,
                text = "Original post"
            )
        )

        val updatedPost = Post(
            id = originalPost.id,
            ownerId = 100,
            fromId = 100,
            date = originalPost.date,
            text = "Updated post"
        )

        val result = service.update(updatedPost)

        assertTrue(result)

        // Проверяем, что текст действительно обновился
        val actualPost = service.getPosts().find { it.id == originalPost.id }
        assertNotNull(actualPost)
        assertEquals("Updated post", actualPost?.text)
    }

    @Test
    fun testUpdateNonExistingPost() {
        val nonExistingPost = Post(
            id = 999,
            ownerId = 100,
            fromId = 100,
            date = System.currentTimeMillis() / 1000,
            text = "Non existing post"
        )

        val result = service.update(nonExistingPost)

        assertFalse(result)
    }

    // ========== Новые тесты для createComment ==========

    @Test
    fun createComment_shouldAddComment_whenPostExists() {
        // Arrange
        val post = Post(
            id = 0,
            ownerId = 100,
            fromId = 100,
            date = System.currentTimeMillis() / 1000,
            text = "Пост для комментария"
        )

        val addedPost = service.add(post)

        val comment = Comment(
            id = 1,
            fromId = 200,
            date = System.currentTimeMillis() / 1000,
            text = "Это тестовый комментарий"
        )

        // Act
        val result = service.createComment(postId = addedPost.id, comment)

        // Assert
        assertEquals(comment, result)
    }

    @Test(expected = PostNotFoundException::class)
    fun createComment_shouldThrowException_whenPostNotFound() {
        // Arrange
        val comment = Comment(
            id = 1,
            fromId = 200,
            date = System.currentTimeMillis() / 1000,
            text = "Комментарий к несуществующему посту"
        )

        // Act
        service.createComment(postId = 999, comment)
    }
}