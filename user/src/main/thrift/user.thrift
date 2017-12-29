namespace * com.aroonpa.thrift

struct User {
  1: string userId,
  2: string password
}


service UserService {
   User addUser(1: User user);
}