package com.github.pathikrit

import org.scalatest._, Matchers._

class MetaRestSpec extends FunSuite {
  import com.github.pathikrit.MetaRest._
  import play.api.libs.json.{Json, Reads, Writes}

  def testJsonRoundTrip[A: Reads : Writes](model: A) = Json.parse(Json.toJson(model).toString()).as[A] shouldEqual model

  test("Generation of Get, Post, Patch, Put models with JSON capabilities") {
    @MetaRest case class User(
      @get                id            : Int,
      @get @post @patch   name          : String,
      @get @post          email         : String,
                          registeredOn  : Long
    )

    testJsonRoundTrip(User.Get(id = 0, name = "Rick", email = "awesome@msn.com"))
    testJsonRoundTrip(User.Post(name = "Rick", email = "awesome@msn.com"))
    "User.Put()" shouldNot compile
    testJsonRoundTrip(User.Patch(name = Some("Pathikrit")))
    "User.Patch()" should compile
  }

  test("Non case classes") {
    "@MetaRest class A" shouldNot compile
    "@MetaRest trait A" shouldNot compile
    "@MetaRest case class A()" should compile
    "@MetaRest object A" shouldNot compile
    //"@MetaRest case class A[T]()" should compile
  }

  //TODO: empty metarest, metarest.Get, other annotations, other annotations called Get?
  /*test("Complex models") {

    sealed trait Document {
      val id: Int
      type Data
    }

    @MetaRest case class Email[A, B](
      @get   override val id             : Int,
      @get @post @patch   subject        : String,
      @put @put           body           : A,
      @get @post @patch   to             : List[String],
      @get @post @patch   cc             : List[String] = Nil,
      @get @post @patch   bcc            : Option[List[String]] = None,
      @get @post      var attachments    : List[B] = Nil
    ) extends Document {
      override type Data = A
    }

    Email.Get(id = 0, subject = "test", to = "me") should beAnInstanceOf[Email.Get]
  }*/
}

