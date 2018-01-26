namespace * com.aroonpa.thrift

struct User {
  1: string userId,
  2: string password
  3: string token
  4: string email

}

struct Login {
  1: string userId,
  2: string password
}

struct OptionalUser {
1: optional User user
}

exception AuthFailed {
    1: string message;
}

exception UserIdAlreadyCreated {
    1: string message;
}

exception UserEmailAlreadyCreated {
    1: string message;
}
service UserService {
   User addUser(1: User user) throws (1: UserIdAlreadyCreated userIdAlreadyCreated, 2:UserEmailAlreadyCreated userEmailAlreadyCreated);
   OptionalUser login(1: Login login) throws (1:AuthFailed authFailed);
   OptionalUser authToUser(1: string token);
}