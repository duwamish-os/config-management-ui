import java.util.UUID
// 1- What is polymorphism?

object Poly {

  def head[A](xs: List[A]): A = xs.head

  def main(args: Array[String]): Unit = {
    println(head(1 :: 1 :: 2 :: 3 :: Nil))

    case class Order(id: UUID, items: List[String])

    println(head(Order(UUID.randomUUID(), List("item1")) :: Order(UUID.randomUUID(), List("item2")) :: Nil))

  }
}
