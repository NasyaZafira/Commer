package com.commer.app.repository

import com.commer.app.data.local.FollowDao
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordBody
import com.commer.app.data.model.remote.settings.account.UpdateAccountBody
import com.commer.app.data.model.remote.signup.SignUpBody
import com.commer.app.data.remote.ApiService
import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val followDao: FollowDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun postSignUpAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        body: SignUpBody,
    ) = flow {
        val response = apiService.postSignUp(body)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun resetPasswordAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        body: ForgotPasswordBody,
    ) = flow {
        val response = apiService.postResetPassword(body)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun codeVerifyAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        body: ForgotPasswordBody,
    ) = flow {
        val response = apiService.codeVerify(body)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun resendPasswordAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        body: ForgotPasswordBody,
    ) = flow {
        val response = apiService.resendPassword(body)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postNewPasswordAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        body: ForgotPasswordBody,
    ) = flow {
        val response = apiService.postChangePassword(body)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getAllPosts(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        onException: () -> Unit,
        statusCode: (code: Int) -> Unit,
        tags: String?
    ) = flow {
        val response = apiService.getPostList(tags)
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
            onException()
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getSuggestedUser(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        onException: () -> Unit,
    ) = flow {
        val response = apiService.getSuggestedUser()
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
            onException()
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getAllUser(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        statusCode: (code: Int) -> Unit,
        fullname: String?
    ) = flow {
        val response = apiService.getAllUsers(fullname)
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.suspendOnError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postFollowUserAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idFollow: Int
    ) = flow {
        val response = apiService.postFollowUser(idFollow)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
            onError(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postUnFollowUserAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idFollow: Int
    ) = flow {
        val response = apiService.postUnFollowUser(idFollow)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postLikePostAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idPost: Int
    ) = flow {
        val response = apiService.postLikePost(idPost)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postUnlikePostAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idPost: Int
    ) = flow {
        val response = apiService.postUnlikePost(idPost)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postDeletePostAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idPost: Int
    ) = flow {
        val response = apiService.postDeletePost(idPost)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postBookmarkPostAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idPost: Int
    ) = flow {
        val response = apiService.postBookmarkPost(idPost)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun deleteBookmarkPostAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idPost: Int
    ) = flow {
        val response = apiService.deleteBookmarkPost(idPost)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postNewPostAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        statusCode: (code: Int) -> Unit,
        status: RequestBody,
        tags: RequestBody,
        desc: RequestBody,
        file: List<MultipartBody.Part>?,
    ) = flow {
        val response = apiService.postNewPost(status, tags, desc, file)
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getDetailPostAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idPost: Int
    ) = flow {
        val response = apiService.getDetailPost(idPost)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postCommentAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idPost: Int,
        fieldComment: String
    ) = flow {
        val response = apiService.postComment(idPost, fieldComment)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun deleteCommentAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idComment: Int
    ) = flow {
        val response = apiService.deleteComment(idComment)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun reportCommentAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idComment: Int,
        reason: String
    ) = flow {
        val response = apiService.postReportComment(idComment, reason)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun reportPostAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idPost: Int,
        reason: String
    ) = flow {
        val response = apiService.postReportPost(idPost, reason)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun detailUserAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idUser: Int
    ) = flow {
        val response = apiService.getDetailUser(idUser)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getFollowingUserAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idUser: Int
    ) = flow {
        val response = apiService.getUserFollowing(idUser)
        response.suspendOnSuccess {
            val follow = this.data.data.map {
                it.toFollowEntity()
            }
            followDao.insertAllFollow(follow)
            emit(follow)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getFollowersUserAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idUser: Int
    ) = flow {
        val response = apiService.getUserFollowers(idUser)
        response.suspendOnSuccess {
            val follow = this.data.data.map {
                it.toFollowEntity()
            }
            followDao.insertAllFollow(follow)
            emit(follow)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun editPostAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        statusCode: (code: Int) -> Unit,
        idPost: Int,
        status: RequestBody,
        tags: RequestBody,
        desc: RequestBody,
    ) = flow {
        val response = apiService.editPost(idPost, status, tags, desc)
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun updateProfileGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        statusCode: (code: Int) -> Unit,
        fullName: RequestBody,
        bio: RequestBody,
        file: List<MultipartBody.Part>?,
    ) = flow {
        val response = apiService.updateProfile(fullName, bio, file)
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getBookmarksAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        statusCode: (code: Int) -> Unit
    ) = flow {
        val response = apiService.getBookmarks()
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun updateAccountAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        statusCode: (code: Int) -> Unit,
        updateAccountBody: UpdateAccountBody
    ) = flow {
        val response = apiService.updateAccount(updateAccountBody)
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getTransactionHistoryAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        statusCode: (code: Int) -> Unit
    ) = flow {
        val response = apiService.getTransactionHistory()
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun reportUserAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        idUser: Int,
        reason: String
    ) = flow {
        val response = apiService.postReportUser(idUser, reason)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getAllVerifiedPost(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        onException: () -> Unit,
        statusCode: (code: Int) -> Unit,
        tags: String?
    ) = flow {
        val response = apiService.getAllVerifiedPost(tags)
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
            onException()
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getOfficialPost(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        onException: () -> Unit,
        statusCode: (code: Int) -> Unit,
        tags: String?
    ) = flow {
        val response = apiService.getOfficialPost(tags)
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
            onException()
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun postSimplerReceipt(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        statusCode: (code: Int) -> Unit,
        plan: RequestBody,
        transaction_id: RequestBody,
        file: List<MultipartBody.Part>?,
    ) = flow {
        val response = apiService.postSimplerReceipts(plan, transaction_id, file)
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

    suspend fun getPaymentStatus(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        statusCode: (code: Int) -> Unit
    ) = flow {
        val response = apiService.getPaymentStatus()
        response.suspendOnSuccess {
            emit(this.data)
            statusCode(this.statusCode.code)
        }.onError {
            Timber.e(this.message())
            onError(this.statusCode.name)
            statusCode(this.statusCode.code)
        }.onException {
            Timber.e(this.message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

}