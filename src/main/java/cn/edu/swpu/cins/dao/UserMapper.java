package cn.edu.swpu.cins.dao;

import cn.edu.swpu.cins.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String Email);

    User checkPasswordByUsername(@Param("username") String username, @Param("password") String password);

    String selectQuestionByUsername(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("newPassword") String newPassword);

    int checkPassword(@Param("password") String password, @Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);
}